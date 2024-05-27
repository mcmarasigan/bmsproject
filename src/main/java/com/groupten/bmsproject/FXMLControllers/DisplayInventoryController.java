package com.groupten.bmsproject.FXMLControllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.groupten.bmsproject.Product.ProductEntity;
import com.groupten.bmsproject.Product.ProductService;

@Controller
public class DisplayInventoryController {

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
    public DisplayInventoryController(ProductService productService) {
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
}
