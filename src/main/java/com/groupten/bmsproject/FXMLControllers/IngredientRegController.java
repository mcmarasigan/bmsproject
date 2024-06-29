package com.groupten.bmsproject.FXMLControllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Ingredient.IngredientService;
import com.groupten.bmsproject.Product.ProductService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
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

    @FXML
    private void handleSaveButton() {
        String ingredient = ingredienttxt.getText();
        Double price = Double.parseDouble(this.price.getText());
        Double quantity = Double.parseDouble(this.quantity.getText());
        LocalDate expiryDate = expirydate.getValue();
        LocalDate dateAdded = LocalDate.now();
        String unitType = UnitTypeIngChoiceBox.getValue();

        String result = ingredientService.addNewIngredient(ingredient, price, quantity, expiryDate, dateAdded, unitType);
        System.out.println(result);
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
