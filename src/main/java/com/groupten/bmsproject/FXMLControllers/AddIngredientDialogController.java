package com.groupten.bmsproject.FXMLControllers;

import com.groupten.bmsproject.Ingredient.IngredientEntity;
import com.groupten.bmsproject.Ingredient.IngredientService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AddIngredientDialogController {

    @FXML
    private ComboBox<String> IngredientNameDialogCombobox;

    @FXML
    private TextField QuantityDialogField;

    @FXML
    private TextField remainingQuantity;

    @FXML
    private TextField unitType;

    @FXML
    private ComboBox<String> UnitTypeDialogCombobox;

    @Autowired
    private IngredientService ingredientService;

    @FXML
private void initialize() {
    populateIngredientNameComboBox();
    populateUnitTypeComboBox();

    IngredientNameDialogCombobox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
            IngredientEntity ingredient = ingredientService.findByName(newValue);
            if (ingredient != null) {
                remainingQuantity.setText(String.valueOf(ingredient.getQuantity()));
                unitType.setText(ingredient.getUnitType());
            }
        }
    });
}

    private void populateIngredientNameComboBox() {
        System.out.println("populateIngredientNameComboBox called"); // Debug log
        List<IngredientEntity> ingredients = ingredientService.getAllIngredients();
        if (ingredients == null || ingredients.isEmpty()) {
            System.out.println("No ingredients found"); // Debug log
        } else {
            for (IngredientEntity ingredient : ingredients) {
                System.out.println("Found ingredient: " + ingredient.getIngredient()); // Debug log
                IngredientNameDialogCombobox.getItems().add(ingredient.getIngredient());
            }
            // Debug log for ComboBox items
            System.out.println("Ingredients added to ComboBox: " + IngredientNameDialogCombobox.getItems());
        }
    }

    private void populateUnitTypeComboBox() {
        UnitTypeDialogCombobox.getItems().addAll("grams", "kilograms");
    }

    @FXML
private void handleAddButton() {
    String ingredientName = IngredientNameDialogCombobox.getValue();
    String quantityStr = QuantityDialogField.getText();

    if (ingredientName != null && !ingredientName.isEmpty() &&
        quantityStr != null && !quantityStr.isEmpty()) {

        Double quantity = Double.parseDouble(quantityStr);

        // Fetch the ingredient entity
        IngredientEntity ingredient = ingredientService.findByName(ingredientName);
        if (ingredient != null) {
            String unitTypeValue = ingredient.getUnitType(); // Retrieve the unit type from the entity
            Double remainingQuantityValue = ingredient.getQuantity(); // Retrieve the remaining quantity from the entity

            // Set the values in the TextField components
            remainingQuantity.setText(String.valueOf(remainingQuantityValue));
            unitType.setText(unitTypeValue);

            if (quantity <= remainingQuantityValue) {
                // Add ingredient to RecipeTable in ProductionScheduleController
                ProductionScheduleController.addIngredientToTable(ingredientName, quantity, unitTypeValue);

                // Show success dialog
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Ingredient Added");
                alert.setHeaderText(null);
                alert.setContentText("Ingredient " + ingredientName + " added successfully!");
                alert.showAndWait();
            } else {
                // Show error dialog for insufficient quantity
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Insufficient Quantity");
                alert.setHeaderText(null);
                alert.setContentText("The selected ingredient does not have enough quantity.");
                alert.showAndWait();
            }
        } else {
            // Show error dialog for zero quantity in ingredient entity
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Ingredient");
            alert.setHeaderText(null);
            alert.setContentText("The selected ingredient is out of stock or invalid.");
            alert.showAndWait();
        }
    } else {
        // Show error dialog for empty fields
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(null);
        alert.setContentText("Please fill in all fields.");
        alert.showAndWait();
    }
}


    @FXML
    private void handleCloseAddIngredients() {
        // Logic to close the dialog
    }
}
