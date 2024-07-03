package com.groupten.bmsproject.FXMLControllers;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Ingredient.IngredientService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.ChoiceBox;

@Component
public class IngredientRegController {
    @FXML
    private TextField ingredienttxt;

    @FXML
    private TextField price;

    @FXML
    private TextField quantity;

    @FXML
    private DatePicker expirydate;

    @FXML
    private ChoiceBox<String> UnitTypeIngChoiceBox;

    @FXML
    private Button savebutton;

    @Autowired
    private IngredientService ingredientService;

    @FXML
    private void initialize() {
        expirydate.setDayCellFactory(getDateCellFactory());
        UnitTypeIngChoiceBox.getItems().addAll("grams", "kilograms");

        // Add listeners to restrict input to numeric values only
        addNumericRestriction(price, true);
        addNumericRestriction(quantity, false);
    }

    private Callback<DatePicker, DateCell> getDateCellFactory() {
        return new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #d48200;");
                        }
                    }
                };
            }
        };
    }

    private void addNumericRestriction(TextField textField, boolean isPrice) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isPrice) {
                if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                    textField.setText(oldValue);
                }
            } else {
                if (!newValue.matches("\\d*")) {
                    textField.setText(oldValue);
                }
            }
        });
    }

    @FXML
    private void handleSaveButton() {
        String ingredient = ingredienttxt.getText();
        String priceStr = price.getText();
        String quantityStr = quantity.getText();
        LocalDate expiryDate = expirydate.getValue();
        String unitType = UnitTypeIngChoiceBox.getValue();

        if (ingredient == null || ingredient.isEmpty()) {
            showAlert(AlertType.ERROR, "Invalid Input", "Please enter the ingredient name.");
            return;
        }

        if (priceStr == null || priceStr.isEmpty()) {
            showAlert(AlertType.ERROR, "Invalid Input", "Please enter the price.");
            return;
        }

        if (quantityStr == null || quantityStr.isEmpty()) {
            showAlert(AlertType.ERROR, "Invalid Input", "Please enter the quantity.");
            return;
        }

        if (expiryDate == null) {
            showAlert(AlertType.ERROR, "Invalid Input", "Please select the expiry date.");
            return;
        }

        if (unitType == null || unitType.isEmpty()) {
            showAlert(AlertType.ERROR, "Invalid Input", "Please select the unit type.");
            return;
        }

        try {
            Double price = Double.parseDouble(priceStr);
            Double quantity = Double.parseDouble(quantityStr);

            if (price <= 0) {
                showAlert(AlertType.ERROR, "Invalid Input", "Price must be greater than zero.");
                return;
            }

            if (quantity <= 0) {
                showAlert(AlertType.ERROR, "Invalid Input", "Quantity must be greater than zero.");
                return;
            }

            LocalDate dateAdded = LocalDate.now();

            String result = ingredientService.addNewIngredient(ingredient, price, quantity, expiryDate, dateAdded, unitType);
            System.out.println(result);

            showAlert(AlertType.INFORMATION, "Success", "Ingredient added successfully!");

        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Invalid Input", "Please enter a valid number for price and quantity.");
        }
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void backtoDashboard() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
