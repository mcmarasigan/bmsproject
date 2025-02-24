package com.groupten.bmsproject.FXMLControllers;

import com.groupten.bmsproject.Ingredient.IngredientEntity;
import com.groupten.bmsproject.Ingredient.IngredientService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EditIngredientDialogController {

    @FXML
    private ComboBox<String> IngredientNameDialogCombobox;

    @FXML
    private TextField QuantityDialogField;

    @FXML
    private TextField unitType;

    @Autowired
    private IngredientService ingredientService;

    private EditProductions editProductionsController;

    @FXML
    private void initialize() {
        populateIngredientNameComboBox();

        IngredientNameDialogCombobox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                IngredientEntity ingredient = ingredientService.findByName(newValue);
                if (ingredient != null) {
                    unitType.setText(ingredient.getUnitType());
                }
            }
        });
    }

    public void setEditProductionsController(EditProductions editProductionsController) {
        this.editProductionsController = editProductionsController;
    }

    private void populateIngredientNameComboBox() {
        List<IngredientEntity> ingredients = ingredientService.getAllIngredients();
        for (IngredientEntity ingredient : ingredients) {
            if (!"archived".equalsIgnoreCase(ingredient.getStatus()) &&
                !"Expired".equalsIgnoreCase(ingredient.getExpiryStatus())) {
                IngredientNameDialogCombobox.getItems().add(ingredient.getIngredient());
            }
        }
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
                    String unitTypeValue = ingredient.getUnitType(); // Retrieve the unit type from the IngredientEntity

                    // Add ingredient to the table in EditProductions
                    editProductionsController.addIngredientToTable(ingredientName, quantity, unitTypeValue);

                    // Show success dialog
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Ingredient Added");
                    alert.setHeaderText(null);
                    alert.setContentText("Ingredient " + ingredientName + " added successfully!");
                    alert.showAndWait();

                    Stage stage = (Stage) QuantityDialogField.getScene().getWindow();
                    stage.close();
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
        Stage stage = (Stage) QuantityDialogField.getScene().getWindow();
        stage.close();
    }
}
