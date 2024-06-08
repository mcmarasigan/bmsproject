package com.groupten.bmsproject.FXMLControllers;

import java.io.IOException;
import java.time.LocalDateTime;
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
import com.groupten.bmsproject.Inventory.InventoryEntity;
import com.groupten.bmsproject.Inventory.InventoryService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

@Controller
public class IngredientSearchTabController {

    @FXML
    private TableView<InventoryEntity> ingredientsTable;
    
    @FXML
    private TableColumn<InventoryEntity, Integer> ingredientID;

    @FXML
    private TableColumn<InventoryEntity, String> ingredientName;

    @FXML
    private TableColumn<InventoryEntity, Double> ingredientPrice;

    @FXML
    private TableColumn<InventoryEntity, Integer> ingredientQuantity;

    @FXML
    private TableColumn<InventoryEntity, LocalDateTime> ingredientExpiry;

    private InventoryEntity selectedIngredient;

    @FXML
    private TextField searchField;

    private final InventoryService inventoryService;

    private ObservableList<InventoryEntity> inventoryList;
    

    @Autowired
    public IngredientSearchTabController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @FXML
    private void initialize() {
        ingredientID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        ingredientName.setCellValueFactory(cellData -> cellData.getValue().IngredientProperty());
        ingredientPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        ingredientQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityIngredientProperty().asObject());
        ingredientExpiry.setCellValueFactory(cellData -> cellData.getValue().expiryTimeProperty());

        populateTable();

        // Add a listener to capture the selected row
        ingredientsTable.setRowFactory(tv -> {
            TableRow<InventoryEntity> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                    selectedIngredient = row.getItem();
                }
            });
            return row;
        });

        // Add a listener to the search field to perform search on text change
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchIngredients(newValue));

    }

    private void populateTable() {
        // Initialize inventoryList with data from inventoryService
        inventoryList = FXCollections.observableArrayList(inventoryService.getAllProducts());
        ingredientsTable.getItems().addAll(inventoryService.getAllProducts());
    }

    private void searchIngredients(String query) {
        List<InventoryEntity> filteredList = inventoryList.stream()
                .filter(ingredient -> ingredient.getIngredient().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        ingredientsTable.setItems(FXCollections.observableArrayList(filteredList));
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
