package com.groupten.bmsproject.FXMLControllers;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.Product.ProductService;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

@Component
public class ProductRegController {
    
    @FXML
    private TextField prdctname;

    @FXML
    private TextField description;

    @FXML
    private TextField pricetxt;

    @FXML
    private DatePicker expiry;

    @FXML
    private TextField quantity;

    @FXML
    private Button selectimg;

    @FXML
    private TextField imgdirectory;

    @Autowired
    private ProductService prdctService;

    @FXML
private void handleSaveButton() {
    String productname = prdctname.getText();
    String productdesc = description.getText();
    String priceString = pricetxt.getText();
    Double price = Double.parseDouble(priceString);
    String quantityString = quantity.getText();
    int quantity = Integer.parseInt(quantityString);

    // Retrieve the selected date from the DatePicker
    LocalDate expiryDate = expiry.getValue();

    // Set the expiry time to the selected date at midnight
    LocalDateTime productexpiry = expiryDate.atStartOfDay();

    String imagelocation = imgdirectory.getText();

    String result = prdctService.addNewProduct(productname, productdesc, price, productexpiry, quantity, imagelocation);

    System.out.println(result);
}

@FXML
    private void handleSelectImageButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        
        File selectedFile = fileChooser.showOpenDialog(selectimg.getScene().getWindow());
        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            imgdirectory.setText(imagePath);
        }
    }
    
}
