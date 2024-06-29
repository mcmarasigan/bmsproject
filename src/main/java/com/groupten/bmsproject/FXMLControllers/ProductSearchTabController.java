package com.groupten.bmsproject.FXMLControllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Product.ProductEntity;
import com.groupten.bmsproject.Product.ProductEntity.QuantityType;
import com.groupten.bmsproject.Product.ProductService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

@Controller
public class ProductSearchTabController {

    @FXML
    private TableView<ProductEntity> productsTable;
    
    @FXML
    private TableColumn<ProductEntity, Integer> productID;

    @FXML
    private TableColumn<ProductEntity, String> productName;

    @FXML
    private TableColumn<ProductEntity, String> productDescription;

    @FXML
    private TableColumn<ProductEntity, Double> productPrice;

    @FXML
    private TableColumn<ProductEntity, QuantityType> qtypeColumn;

    @FXML
    private TableColumn<ProductEntity, Integer> productQuantity;

    @FXML
    private TableColumn<ProductEntity, LocalDate> productExpiry;

    @FXML
    private TableColumn<ProductEntity, String> productImage;

    private ProductEntity selectedProduct;

    @FXML
    private TextField searchField;

    private final ProductService productService;

    private ObservableList<ProductEntity> productList;
    

    @Autowired
    public ProductSearchTabController(ProductService productService) {
        this.productService = productService;
    }

    @FXML
    private void initialize() {
        productID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        productDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        productImage.setCellValueFactory(cellData -> cellData.getValue().imglocationProperty());
        productPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        qtypeColumn.setCellValueFactory(cellData -> cellData.getValue().quantityTypeProperty());
        productName.setCellValueFactory(cellData -> cellData.getValue().productnameProperty());

        populateTable();

        // Add a listener to capture the selected row
        productsTable.setRowFactory(tv -> {
            TableRow<ProductEntity> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                    selectedProduct = row.getItem();
                    try {
                        proceedtoProduct();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

        // Add a listener to the search field to perform search on text change
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchProducts(newValue));

    }

    private void populateTable() {
        productList = FXCollections.observableArrayList(
            productService.getAllProducts().stream()
                .filter(product -> !"archived".equals(product.getStatus()))
                .collect(Collectors.toList())
        );
        productsTable.setItems(productList);
    }

    private void searchProducts(String query) {
        List<ProductEntity> filteredList;
        try {
            int id = Integer.parseInt(query);
            filteredList = productList.stream()
                    .filter(product -> product.getID() == id)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            filteredList = productList.stream()
                    .filter(product -> product.getproductName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }
        productsTable.setItems(FXCollections.observableArrayList(filteredList));
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

    @FXML
    private String getsearchText() {
        return searchField.getText();
    }

    @FXML
    private void proceedtoProduct() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayProducts.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();

        // Get the controller and set the search text
        DisplayProductController controller = loader.getController();
        controller.setSearchTextField(getsearchText());
        controller.setSelectedProduct(selectedProduct);

        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
