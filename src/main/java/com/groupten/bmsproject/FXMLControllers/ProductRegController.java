package com.groupten.bmsproject.FXMLControllers;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Admin.AdminService;
import com.groupten.bmsproject.Product.ProductService;
import com.groupten.bmsproject.Product.ProductEntity.QuantityType;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import java.util.function.UnaryOperator;

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

    private final ProductService prdctService;
    private final AdminService adminService;

    @Autowired
    public ProductRegController(ProductService prdctService, AdminService adminService) {
        this.prdctService = prdctService;
        this.adminService = adminService;
    }

    @FXML
    public void initialize() {
        // Initialize the ComboBox with QuantityType enum values
        quantityCombobox.getItems().setAll(QuantityType.values());

        // Set text formatters for the text fields
        setTextFormatter(prdctname, "[a-zA-Z0-9 ]*");
        setTextFormatter(description, "[a-zA-Z0-9,.!? ]*");
        setPriceTextFormatter(pricetxt);
    }

    @FXML
    private void handleSaveButton() {
        String productname = prdctname.getText();
        String productdesc = description.getText();
        String priceString = pricetxt.getText();
        QuantityType type = quantityCombobox.getValue();
        Double price = null;

        // Validate inputs
        if (productname == null || productname.trim().isEmpty()) {
            showAlert(AlertType.ERROR, "Invalid Input", "Product name cannot be empty.");
            return;
        }

        if (productdesc == null || productdesc.trim().isEmpty()) {
            showAlert(AlertType.ERROR, "Invalid Input", "Product description cannot be empty.");
            return;
        }

        try {
            price = Double.parseDouble(priceString);
            // Format price to 2 decimal places
            price = new BigDecimal(price).setScale(2, RoundingMode.HALF_UP).doubleValue();
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Invalid Input", "Price must be a valid number.");
            return;
        }

        if (type == null) {
            showAlert(AlertType.ERROR, "Invalid Input", "Quantity type must be selected.");
            return;
        }

        LocalDate dateAdded = LocalDate.now();
        String imagelocation = imgdirectory.getText();

        String result = prdctService.addNewProduct(productname, productdesc, price, type, imagelocation, dateAdded);
        showAlert(AlertType.INFORMATION, "Product Registration", result);

        String loggedInUser = adminService.getLoggedInUser(); // Retrieve logged-in user info
        System.out.println("Logged-in User: " + loggedInUser);
    }

    @FXML
    private void handleSelectImageButton() {
        String imagePath = selectImage();
        if (imagePath != null) {
            imgdirectory.setText(imagePath);
        }
    }

    private String selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = fileChooser.showOpenDialog(selectimg.getScene().getWindow());
        return (selectedFile != null) ? selectedFile.getAbsolutePath() : null;
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

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setTextFormatter(TextField textField, String pattern) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (change.getControlNewText().matches(pattern)) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }

    private void setPriceTextFormatter(TextField textField) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        };
        textField.setTextFormatter(new TextFormatter<>(filter));
    }
}
