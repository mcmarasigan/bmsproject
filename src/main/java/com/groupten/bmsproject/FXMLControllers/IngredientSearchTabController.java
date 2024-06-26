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
import com.groupten.bmsproject.Ingredient.IngredientEntity;
import com.groupten.bmsproject.Ingredient.IngredientService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

@Controller
public class IngredientSearchTabController {

    @FXML
    private TableView<IngredientEntity> ingredientsTable;
    
    @FXML
    private TableColumn<IngredientEntity, Integer> ingredientID;

    @FXML
    private TableColumn<IngredientEntity, String> ingredientName;

    @FXML
    private TableColumn<IngredientEntity, Double> ingredientPrice;

    @FXML
    private TableColumn<IngredientEntity, Double> ingredientQuantity;

    @FXML
    private TableColumn<IngredientEntity, LocalDate> ingredientExpiry;

    private IngredientEntity selectedIngredient;

    @FXML
    private TextField searchField;

    private final IngredientService inventoryService;

    private ObservableList<IngredientEntity> inventoryList;
    

    @Autowired
    public IngredientSearchTabController(IngredientService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @FXML
    private void initialize() {
        // Retrieve from database
        ingredientID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        ingredientName.setCellValueFactory(cellData -> cellData.getValue().IngredientProperty());
        ingredientPrice.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        ingredientQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityIngredientProperty().asObject());
        ingredientExpiry.setCellValueFactory(cellData -> cellData.getValue().expiryDateProperty());

        populateTable();

        // Add a listener to capture the selected row
        ingredientsTable.setRowFactory(tv -> {
            TableRow<IngredientEntity> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                    selectedIngredient = row.getItem();
                    try {
                        proceedtoInventory();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

        // Add a listener to the search field to perform search on text change
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchIngredients(newValue));

    }
    // Populates the table
    private void populateTable() {
        // Initialize inventoryList with data from inventoryService
        inventoryList = FXCollections.observableArrayList(inventoryService.getAllProducts());
        ingredientsTable.getItems().addAll(inventoryService.getAllProducts());
    }

    // Searches the table
    private void searchIngredients(String query) {
        List<IngredientEntity> filteredList;
        try {
            int id = Integer.parseInt(query);
            filteredList = inventoryList.stream()
                    .filter(ingredient -> ingredient.getID() == id)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            filteredList = inventoryList.stream()
                    .filter(ingredient -> ingredient.getIngredient().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }
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

    @FXML
    private String getsearchText() {
        return searchField.getText();
    }

    @FXML
    private void proceedtoInventory() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayIngredients.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();

        // Get the controller and set the search text
        DisplayIngredientController controller = loader.getController();
        controller.setSearchTextField(getsearchText());
        controller.setSelectedIngredient(selectedIngredient);

        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
