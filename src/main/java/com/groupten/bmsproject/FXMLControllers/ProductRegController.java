package com.groupten.bmsproject.FXMLControllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Order.OrderEntity.PaymentStatus;
import com.groupten.bmsproject.Product.ProductService;
import com.groupten.bmsproject.Product.ProductEntity.QuantityType;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;

@Component
public class ProductRegController {
    
    @FXML
    private TextField prdctname;

    @FXML
    private TextField description;

    @FXML
    private TextField pricetxt;

    @FXML
    private Button selectimg;

    @FXML
    private TextField imgdirectory;

    @FXML
    private ComboBox<QuantityType> quantityCombobox;

    @Autowired
    private ProductService prdctService;

    @FXML
    public void initialize() {
        // Initialize the ComboBox with QuantityType enum values
        quantityCombobox.getItems().setAll(QuantityType.values());
    }

    @FXML
private void handleSaveButton() {
    String productname = prdctname.getText();
    String productdesc = description.getText();
    String priceString = pricetxt.getText();
    QuantityType type = quantityCombobox.getValue();
    Double price = Double.parseDouble(priceString);

    LocalDate dateAdded = LocalDate.now();

    String imagelocation = imgdirectory.getText();

    String result = prdctService.addNewProduct(productname, productdesc, price, type, imagelocation, dateAdded);

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
