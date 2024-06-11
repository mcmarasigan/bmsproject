package com.groupten.bmsproject.FXMLControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import com.groupten.bmsproject.BmsprojectApplication;
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
    private StackPane editPane;

    @FXML
    private TextField editNamefield;

    @FXML
    private TextField editPricefield;

    @FXML
    private TextField editQuantityfield;

    @FXML
    private DatePicker editExpiryfield;

    private InventoryEntity selectedIngredient;

    @FXML
    private TextField SearchTextfield;

    private final InventoryService inventoryService;

    private ObservableList<InventoryEntity> inventoryList;
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

        populateTable();

        // Add a listener to capture the selected row
        IngredientTable.setRowFactory(tv -> {
            TableRow<InventoryEntity> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                    selectedIngredient = row.getItem();
                }
            });
            return row;
        });

        // Add a listener to the search field to perform search on text change
        SearchTextfield.textProperty().addListener((observable, oldValue, newValue) -> searchIngredients(newValue));
    }


    private void populateTable() {
        inventoryList = FXCollections.observableArrayList(inventoryService.getAllProducts());
        IngredientTable.getItems().addAll(inventoryService.getAllProducts());
    }

    private void searchIngredients(String query) {
        List<InventoryEntity> filteredList = inventoryList.stream()
                .filter(ingredient -> ingredient.getIngredient().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        IngredientTable.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    private void handleEditButton() {
        if (selectedIngredient != null) {
            // Fill the fields with the selected ingredient details
            editNamefield.setText(selectedIngredient.getIngredient());
            editPricefield.setText(selectedIngredient.getPrice().toString());
            editQuantityfield.setText(selectedIngredient.getQuantity().toString());
            editExpiryfield.setValue(selectedIngredient.getExpiry().toLocalDate());
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
        Integer id = selectedIngredient.getID();
        String ingredient = editNamefield.getText();
        String priceString = editPricefield.getText();
        Double price = Double.parseDouble(priceString);
        String quantityString = editQuantityfield.getText();
        int quantity = Integer.parseInt(quantityString);

        // Retrieve the selected date from the DatePicker
        LocalDate expiryDate = editExpiryfield.getValue();

        // Set the expiry time to the selected date at midnight
        LocalDateTime productexpiry = expiryDate.atStartOfDay();

        String result = inventoryService.updateIngredient(id, ingredient, price, quantity, productexpiry);

        System.out.println(result);
        
        // Refresh the table to show the updated details
        IngredientTable.refresh();
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
    private void proceedtoIngredientreg() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ingredientregistration.fxml"));
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
    public void setSelectedIngredient(InventoryEntity selectedIngredient) {
        this.selectedIngredient = selectedIngredient;
        displaySelectedIngredient();
    }

    //Displays the selected row
    private void displaySelectedIngredient() {
        // Set the table's items to only the selected ingredient
        IngredientTable.setItems(FXCollections.observableArrayList(selectedIngredient));
    }
}