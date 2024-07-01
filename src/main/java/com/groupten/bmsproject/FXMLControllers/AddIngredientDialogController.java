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
    private ComboBox<String> UnitTypeDialogCombobox;

    @Autowired
    private IngredientService ingredientService;

    private ProductionScheduleController productionScheduleController;

    @FXML
    private void initialize() {
        populateIngredientNameComboBox();
        populateUnitTypeComboBox();

        IngredientNameDialogCombobox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Optionally, you can still fetch and display unit type if needed
                // IngredientEntity ingredient = ingredientService.findByName(newValue);
                // if (ingredient != null) {
                //     unitType.setText(ingredient.getUnitType());
                // }
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
                if (!"archived".equalsIgnoreCase(ingredient.getStatus())) {
                    System.out.println("Found active ingredient: " + ingredient.getIngredient()); // Debug log
                    IngredientNameDialogCombobox.getItems().add(ingredient.getIngredient());
            }
            // Debug log for ComboBox items
            System.out.println("Ingredients added to ComboBox: " + IngredientNameDialogCombobox.getItems());
        }
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

            try {
                Double quantity = Double.parseDouble(quantityStr);

                // Validate quantity
                if (quantity <= 0) {
                    // Show error dialog for zero or negative quantity
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Invalid Quantity");
                    alert.setHeaderText(null);
                    alert.setContentText("Quantity must be greater than zero.");
                    alert.showAndWait();
                    return; // Exit method if quantity is invalid
                }

                // Fetch the ingredient entity
                IngredientEntity ingredient = ingredientService.findByName(ingredientName);
                if (ingredient != null) {
                    String unitTypeValue = UnitTypeDialogCombobox.getValue(); // Retrieve the unit type from the ComboBox

                    // Add ingredient to the table in ProductionScheduleController
                    productionScheduleController.addIngredientToTable(ingredientName, quantity, unitTypeValue);

                    // Show success dialog
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Ingredient Added");
                    alert.setHeaderText(null);
                    alert.setContentText("Ingredient " + ingredientName + " added successfully!");
                    alert.showAndWait();
                } else {
                    // Show error dialog for invalid ingredient selection
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Invalid Ingredient");
                    alert.setHeaderText(null);
                    alert.setContentText("The selected ingredient is out of stock or invalid.");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                // Show error dialog for non-numeric quantity
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Quantity");
                alert.setHeaderText(null);
                alert.setContentText("Quantity must be a valid number.");
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

    public void setProductionScheduleController(ProductionScheduleController productionScheduleController) {
        this.productionScheduleController = productionScheduleController;
    }
}
