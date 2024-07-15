package com.groupten.bmsproject.FXMLControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Ingredient.IngredientEntity;
import com.groupten.bmsproject.Ingredient.IngredientService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DisplayIngredientController {
    
    @FXML
    private TableView<IngredientEntity> IngredientTable;

    @FXML
    private TableColumn<IngredientEntity, Integer> idColumn;

    @FXML
    private TableColumn<IngredientEntity, String> IngredientNameColumn;

    @FXML
    private TableColumn<IngredientEntity, Double> PriceColumn;

    @FXML
    private TableColumn<IngredientEntity, Double> QuantityColumn;

    @FXML
    private TableColumn<IngredientEntity, LocalDate> ExpiryDateColumn;
    
    @FXML
    private TableColumn<IngredientEntity, String> UnitTypeIngColumn;

    @FXML
    private TableColumn<IngredientEntity, Integer> daystillexpireColumn;

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

    private IngredientEntity selectedIngredient;

    @FXML
    private TextField SearchTextfield;

    private final IngredientService ingredientService;

    private ObservableList<IngredientEntity> ingredientList;

    @Autowired
    public DisplayIngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @FXML
private void initialize() {
    editExpiryfield.setDayCellFactory(getDateCellFactory());

    idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
    IngredientNameColumn.setCellValueFactory(cellData -> cellData.getValue().IngredientProperty());
    PriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
    QuantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityIngredientProperty().asObject());
    UnitTypeIngColumn.setCellValueFactory(cellData -> cellData.getValue().unitTypeProperty());
    ExpiryDateColumn.setCellValueFactory(cellData -> cellData.getValue().expiryDateProperty());


    // Update all ingredients expiry and days until expiration
    ingredientService.updateAllIngredients();

    populateTable();

    // Set row factory to mark expired rows red
    IngredientTable.setRowFactory(tv -> {
        TableRow<IngredientEntity> row = new TableRow<>();
        row.itemProperty().addListener((obs, oldIngredient, newIngredient) -> {
            if (newIngredient == null) {
                row.setStyle("");
            } else {
                String status = newIngredient.getExpiryStatus();
                if ("Expired".equals(status)) {
                    row.setStyle("-fx-background-color: red;"); // Red background for expired ingredients
                } else {
                    row.setStyle("");
                }
            }
        });

        row.setOnMouseClicked(event -> {
            if (!row.isEmpty() && event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                selectedIngredient = row.getItem();
            }
        });

        return row;
    });

    SearchTextfield.textProperty().addListener((observable, oldValue, newValue) -> searchIngredients(newValue));
}


    private Callback<DatePicker, DateCell> getDateCellFactory() {
        return new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(LocalDate.now())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #d48200;");
                        }
                    }
                };
            }
        };
    }

    private void populateTable() {
        ingredientList = FXCollections.observableArrayList(
                ingredientService.getAllProducts().stream()
                        .filter(ingredient -> !"archived".equalsIgnoreCase(ingredient.getStatus()))
                        .collect(Collectors.toList())
        );
        IngredientTable.setItems(ingredientList);
    }

    private void searchIngredients(String query) {
        List<IngredientEntity> filteredList = ingredientList.stream()
                .filter(ingredient -> ingredient.getIngredient().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        IngredientTable.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    private void handleEditButton() {
        if (selectedIngredient != null) {
            editPane.setVisible(true);
            editNamefield.setText(selectedIngredient.getIngredient());
            editPricefield.setText(selectedIngredient.getPrice().toString());
            editQuantityfield.setText(selectedIngredient.getQuantity().toString());
            editExpiryfield.setValue(selectedIngredient.getExpiryDate());
            // Add handling for the unit type if needed
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
        if (selectedIngredient != null) {
            String newName = editNamefield.getText();
            Double newPrice = Double.parseDouble(editPricefield.getText());
            Double newQuantity = Double.parseDouble(editQuantityfield.getText());
            LocalDate newExpiry = editExpiryfield.getValue();
            // Handle the unit type if necessary

            ingredientService.updateIngredient(selectedIngredient.getID(), newName, newPrice, newQuantity, newExpiry, selectedIngredient.getUnitType());
            populateTable();
            editPane.setVisible(false);
        }
    }

    @FXML
    private void handleCancelButton() {
        editPane.setVisible(false);
    }

    @FXML
    private void handleArchiveButton() {
        if (selectedIngredient != null) {
            // Archive the selected ingredient
            String result = ingredientService.archiveIngredient(selectedIngredient.getID());

            System.out.println(result);

            // Remove the ingredient from the table
            ingredientList.remove(selectedIngredient);
            IngredientTable.refresh();

            // Hide the edit pane if it's visible
            editPane.setVisible(false);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Ingredient Selected");
            alert.setContentText("Please select an ingredient in the table.");
            alert.showAndWait();
        }
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

    // Retrieves the search text from the Ingredient search then 
    public void setSearchTextField(String result) {
        SearchTextfield.setText(result);
    }

    // Sets the selected Ingredient as the row retrieved from the ingredient search
    public void setSelectedIngredient(IngredientEntity selectedIngredient) {
        this.selectedIngredient = selectedIngredient;
        displaySelectedIngredient();
    }

    // Displays the selected row
    private void displaySelectedIngredient() {
        // Set the table's items to only the selected ingredient
        IngredientTable.setItems(FXCollections.observableArrayList(selectedIngredient));
    }
}
