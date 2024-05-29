package com.groupten.bmsproject.FXMLControllers;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.Inventory.InventoryService;
import com.groupten.bmsproject.Product.ProductService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

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
}
