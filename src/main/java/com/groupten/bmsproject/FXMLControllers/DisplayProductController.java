package com.groupten.bmsproject.FXMLControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Ingredient.IngredientEntity;
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
    private TableColumn<ProductEntity, LocalDate> expiryTimeColumn;

    @FXML
    private TableColumn<ProductEntity, String> imgLocationColumn;

    @FXML
    private TableColumn<ProductEntity, Double> priceColumn;

    @FXML
    private TableColumn<ProductEntity, String> productNameColumn;

    @FXML
    private TableColumn<ProductEntity, Integer> quantityColumn;

    @FXML
    private StackPane editPane;
    
    @FXML
    private TextField editProductfield;

    @FXML
    private TextField editDescriptionfield;

    @FXML
    private TextField editQuantityfield;

    @FXML
    private TextField editImagefield;

    @FXML
    private TextField editPricefield;

    @FXML
    private DatePicker editExpiryfield;

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

    // Disable past dates in the DatePicker
    editExpiryfield.setDayCellFactory(getDateCellFactory());


        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productnameProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        imgLocationColumn.setCellValueFactory(cellData -> cellData.getValue().imglocationProperty());
        expiryTimeColumn.setCellValueFactory(cellData -> cellData.getValue().expiryDateProperty());
        
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
    private void handleEditButton() {
        if (selectedProduct != null) {
            // Fill the fields with the selected ingredient details
            editProductfield.setText(selectedProduct.getproductName());
            editDescriptionfield.setText(selectedProduct.prodDescription());
            editQuantityfield.setText(selectedProduct.productQuantity().toString());
            editImagefield.setText(selectedProduct.imageLocation());
            editPricefield.setText(selectedProduct.price().toString());
            editExpiryfield.setValue(selectedProduct.productExpiry());
            // Show the edit pane
            editPane.setVisible(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Ingredient Selected");
            alert.setContentText("Please select an ingredient in the table.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleSaveButton() {
        Integer id = selectedProduct.getID();
        String product = editProductfield.getText();
        String description = editDescriptionfield.getText();
        String imglocation = editImagefield.getText();
        String priceString = editPricefield.getText();
        Double price = Double.parseDouble(priceString);
        String quantityString = editQuantityfield.getText();

        int quantity = Integer.parseInt(quantityString);
        

        // Retrieve the selected date from the DatePicker
        LocalDate expiryDate = editExpiryfield.getValue();

        String result = productService.updateProduct(id, product, description, price, expiryDate, quantity, imglocation);

        System.out.println(result);
        
        // Refresh the table to show the updated details
        productTable.refresh();
        // Hide the edit pane
        editPane.setVisible(false);
    }

    @FXML
    private void handleCancelButton() {
        // Hide the edit pane without saving
        editPane.setVisible(false);
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
    public void setSelectedProduct(ProductEntity selectedProduct) {
        this.selectedProduct = selectedProduct;
        displaySelectedProduct();
    }

    //Displays the selected row
    private void displaySelectedProduct() {
        // Set the table's items to only the selected ingredient
        productTable.setItems(FXCollections.observableArrayList(selectedProduct));
    }
}
