package com.groupten.bmsproject.FXMLControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.groupten.bmsproject.ProductionSchedule.ProductionScheduleEntity;
import com.groupten.bmsproject.ProductionSchedule.ProductionScheduleService;
import com.groupten.bmsproject.Product.ProductEntity;
import com.groupten.bmsproject.Product.ProductService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
public class ProductionScheduleController {

    @FXML
    private ChoiceBox<String> productChoiceBox;

    @FXML
    private TextField quantityField;

    @FXML
    private DatePicker dateofproductionPicker;

    @FXML
    private DatePicker expdatePicker;

    @FXML
    private Button savebutton;

    private final ProductionScheduleService productionScheduleService;
    private final ProductService productService;

    @Autowired
    public ProductionScheduleController(ProductionScheduleService productionScheduleService, ProductService productService) {
        this.productionScheduleService = productionScheduleService;
        this.productService = productService;
    }

    @FXML
    private void initialize() {
        populateProductChoiceBox();

        savebutton.setOnAction(event -> handleSaveButton());
    }

    private void populateProductChoiceBox() {
        List<ProductEntity> products = productService.getAllProducts();
        for (ProductEntity product : products) {
            productChoiceBox.getItems().add(product.getproductName());
        }
    }

    @FXML
private void handleSaveButton() {
    String productName = productChoiceBox.getValue();
    int quantity = Integer.parseInt(quantityField.getText());
    LocalDate dateOfProduction = dateofproductionPicker.getValue();
    LocalDate expDate = expdatePicker.getValue();

    // Calculate the number of days between the production date and the expiration date
    long numberOfDays = ChronoUnit.DAYS.between(dateOfProduction, expDate);

    ProductionScheduleEntity productionSchedule = new ProductionScheduleEntity();
    productionSchedule.setproductName(productName);
    productionSchedule.setproductschedQuantity(quantity);
    productionSchedule.setdateofProduction(dateOfProduction.atStartOfDay());
    productionSchedule.setexpDate(expDate.atStartOfDay());
    productionSchedule.setnumberofdaysexp((int) numberOfDays); // Set the number of days

    String levelOfStock = quantity < 50 ? "Low" : "Sufficient";
    productionSchedule.setlvlofstock(levelOfStock);

    productionScheduleService.save(productionSchedule);
}

}
