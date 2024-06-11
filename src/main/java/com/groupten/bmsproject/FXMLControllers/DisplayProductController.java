package com.groupten.bmsproject.FXMLControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Inventory.InventoryEntity;
import com.groupten.bmsproject.Product.ProductEntity;
import com.groupten.bmsproject.Product.ProductService;

@Controller
public class DisplayProductController {

    @FXML
    private TableView<ProductEntity> productTable;

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

    @FXML
    private TextField SearchTextfield;

    private final ProductService productService;

    private ObservableList<ProductEntity> productList;

    private ProductEntity selectedProduct;

    @Autowired
    public DisplayProductController(ProductService productService) {
        this.productService = productService;
    }

    @FXML
private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productnameProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        imgLocationColumn.setCellValueFactory(cellData -> cellData.getValue().imglocationProperty());
        expiryTimeColumn.setCellValueFactory(cellData -> cellData.getValue().expiryTimeProperty());
        
        populateTable();

        // Add a listener to capture the selected row
        productTable.setRowFactory(tv -> {
            TableRow<ProductEntity> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                    selectedProduct = row.getItem();
                }
            });
            return row;
        });

        // Add a listener to the search field to perform search on text change
        SearchTextfield.textProperty().addListener((observable, oldValue, newValue) -> searchProducts(newValue));
    }

    private void populateTable() {
        productList = FXCollections.observableArrayList(productService.getAllProducts());
        productTable.getItems().addAll(productService.getAllProducts());
    }

    private void searchProducts(String query) {
        List<ProductEntity> filteredList = productList.stream()
                .filter(product -> product.getproductName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        productTable.setItems(FXCollections.observableArrayList(filteredList));
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

    //Retrives the search text from the Ingredient search then 
    public void setSearchTextField(String result) {
        SearchTextfield.setText(result);
    }

    //Sets the selected Ingredient as the row retrieved from the ingredient search
    public void setSelectedIngredient(ProductEntity selectedProduct) {
        this.selectedProduct = selectedProduct;
        displaySelectedIngredient();
    }

    //Displays the selected row
    private void displaySelectedIngredient() {
        // Set the table's items to only the selected ingredient
        productTable.setItems(FXCollections.observableArrayList(selectedProduct));
    }
}
