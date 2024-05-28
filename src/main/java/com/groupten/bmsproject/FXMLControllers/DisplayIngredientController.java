package com.groupten.bmsproject.FXMLControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.groupten.bmsproject.Inventory.InventoryEntity;
import com.groupten.bmsproject.Inventory.InventoryService;

@Controller
public class DisplayIngredientController {
    
    @FXML
    private TableView<InventoryEntity> IngredientTable;

    @FXML
    private TableColumn<InventoryEntity, Integer> idColumn;

    @FXML
    private TableColumn<InventoryEntity, String> IngredientNameColumn;

    @FXML
    private TableColumn<InventoryEntity, Double> PriceColumn;

    @FXML
    private TableColumn<InventoryEntity, Integer> QuantityColumn;

    @FXML
    private TableColumn<InventoryEntity, LocalDateTime> ExpiryDateColumn;

    @FXML
    private TextField SearchTextfield;

    private final InventoryService inventoryService;
    private ObservableList<InventoryEntity> masterData = FXCollections.observableArrayList();

    @Autowired
    public DisplayIngredientController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        IngredientNameColumn.setCellValueFactory(cellData -> cellData.getValue().IngredientProperty());
        PriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        QuantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityIngredientProperty().asObject());
        ExpiryDateColumn.setCellValueFactory(cellData -> cellData.getValue().expiryTimeProperty());

        // Populate the masterData list with data from the inventory service
        masterData.addAll(inventoryService.getAllProducts());

        // Wrap the ObservableList in a FilteredList
        FilteredList<InventoryEntity> filteredData = new FilteredList<>(masterData, p -> true);

        // Add a listener to the SearchTextField to filter the data
        SearchTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ingredient -> {
                // If the search field is empty, display all ingredients
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare ingredient details with the filter text
                String lowerCaseFilter = newValue.toLowerCase();

                if (ingredient.getIngredient().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ingredient name
                } else if (ingredient.getPrice().toString().contains(lowerCaseFilter)) {
                    return true; // Filter matches price
                } else if (ingredient.getQuantity().toString().contains(lowerCaseFilter)) {
                    return true; // Filter matches quantity
                }
                return false; // Does not match
            });
        });

        // Bind the FilteredList to the TableView
        IngredientTable.setItems(filteredData);
    }
}
