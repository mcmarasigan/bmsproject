package com.groupten.bmsproject.FXMLControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Product.ProductEntity;
import com.groupten.bmsproject.Product.ProductService;

@Controller
public class DisplayProductController {

    @FXML
    private TableView<ProductEntity> inventoryTable;

    @FXML
    private TableColumn<ProductEntity, Integer> idColumn;

    @FXML
    private TableColumn<ProductEntity, String> descriptionColumn;

    @FXML
    private TableColumn<ProductEntity, LocalDateTime> expiryTimeColumn;

    @FXML
    private TableColumn<ProductEntity, String> imgLocationColumn;

    @FXML
    private TableColumn<ProductEntity, Double> priceColumn;

    @FXML
    private TableColumn<ProductEntity, String> productNameColumn;

    @FXML
    private TableColumn<ProductEntity, Integer> quantityColumn;

    private final ProductService productService;

    @Autowired
    public DisplayProductController(ProductService productService) {
        this.productService = productService;
    }

    @FXML
private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        expiryTimeColumn.setCellValueFactory(cellData -> cellData.getValue().expiryTimeProperty());
        imgLocationColumn.setCellValueFactory(cellData -> cellData.getValue().imglocationProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productnameProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());

        populateTable();
    }

    private void populateTable() {
        inventoryTable.getItems().addAll(productService.getAllProducts());
    }

    @FXML
    private void backtoInventory() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Inventory.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void proceedtoProductreg() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Productregistration.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
