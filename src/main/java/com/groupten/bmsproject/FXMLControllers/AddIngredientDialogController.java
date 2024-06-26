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

    @FXML
    private void initialize() {
        populateIngredientNameComboBox();
        populateUnitTypeComboBox();
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
        String unitType = UnitTypeDialogCombobox.getValue();

        if (ingredientName != null && !ingredientName.isEmpty() &&
            quantityStr != null && !quantityStr.isEmpty() &&
            unitType != null && !unitType.isEmpty()) {

            int quantity = Integer.parseInt(quantityStr);

            // Add ingredient to RecipeTable in ProductionScheduleController
            ProductionScheduleController.addIngredientToTable(ingredientName, quantity, unitType);

            // Show success dialog
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Ingredient Added");
            alert.setHeaderText(null);
            alert.setContentText("Ingredient " + ingredientName + " added successfully!");
            alert.showAndWait();

        } else {
            // Show error dialog
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
