package com.groupten.bmsproject.FXMLControllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Product.ProductService;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private void initialize() {
        // Disable past dates in the DatePicker
        expiry.setDayCellFactory(getDateCellFactory());
    }

    private Callback<DatePicker, DateCell> getDateCellFactory() {
        return new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        // Disable all past dates
                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #d48200;"); // You can set a style to indicate disabled dates
                        }
                    }
                };
            }
        };
    }

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
