package com.groupten.bmsproject.FXMLControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import com.groupten.bmsproject.ProductionSchedule.ProductionScheduleEntity;
import com.groupten.bmsproject.ProductionSchedule.ProductionScheduleService;
import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Product.ProductEntity;
import com.groupten.bmsproject.Product.ProductService;

import java.io.IOException;
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

        // Disable past dates in the DatePicker
        expdatePicker.setDayCellFactory(getDateCellFactory());
        dateofproductionPicker.setDayCellFactory(getDateCellFactory());
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
    productionSchedule.setdateofProduction(dateOfProduction);
    productionSchedule.setexpDate(expDate);
    productionSchedule.setnumberofdaysexp((int) numberOfDays); // Set the number of days

    String levelOfStock = quantity < 50 ? "Low" : "Sufficient";
    productionSchedule.setlvlofstock(levelOfStock);

    productionScheduleService.save(productionSchedule);
    
}

 @FXML
    private void goBackProductionSched() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayProductionSchedule.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
