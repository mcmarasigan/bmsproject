package com.groupten.bmsproject.FXMLControllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Inventory.InventoryService;
import com.groupten.bmsproject.Product.ProductService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

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
    private Button savebutton;

    @Autowired
    private InventoryService inventoryService;

    @FXML
    private void handleSaveButton() {
        String ingredient = ingredienttxt.getText();
        String priceString = price.getText();
        Double price = Double.parseDouble(priceString);
        String quantityString = quantity.getText();
        int quantity = Integer.parseInt(quantityString);

        // Retrieve the selected date from the DatePicker
        LocalDate expiryDate = expirydate.getValue();

        // Set the expiry time to the selected date at midnight
        LocalDateTime productexpiry = expiryDate.atStartOfDay();

        String result = inventoryService.addNewInventory(ingredient, price, quantity, productexpiry);

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
