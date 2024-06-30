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
import javafx.stage.Stage;

import java.util.List;

@Controller
public class EditIngredientDialogController {

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

    private EditProductions editProductionsController;

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

    public void setEditProductionsController(EditProductions editProductionsController) {
        this.editProductionsController = editProductionsController;
    }

    private void populateIngredientNameComboBox() {
        List<IngredientEntity> ingredients = ingredientService.getAllIngredients();
        for (IngredientEntity ingredient : ingredients) {
            IngredientNameDialogCombobox.getItems().add(ingredient.getIngredient());
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

            IngredientEntity ingredient = ingredientService.findByName(ingredientName);
            if (ingredient != null) {
                String unitTypeValue = ingredient.getUnitType();
                Double remainingQuantityValue = ingredient.getQuantity();

                if (quantity <= remainingQuantityValue) {
                    editProductionsController.addIngredientToTable(ingredientName, quantity, unitTypeValue);

                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Ingredient Added");
                    alert.setHeaderText(null);
                    alert.setContentText("Ingredient " + ingredientName + " added successfully!");
                    alert.showAndWait();

                    Stage stage = (Stage) QuantityDialogField.getScene().getWindow();
                    stage.close();
                } else {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Insufficient Quantity");
                    alert.setHeaderText(null);
                    alert.setContentText("The selected ingredient does not have enough quantity.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Ingredient");
                alert.setHeaderText(null);
                alert.setContentText("The selected ingredient is out of stock or invalid.");
                alert.showAndWait();
            }
        } else {
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
