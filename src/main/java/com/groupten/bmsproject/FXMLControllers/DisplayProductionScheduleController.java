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
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.ProductionSchedule.ProductionScheduleEntity;
import com.groupten.bmsproject.ProductionSchedule.ProductionScheduleService;
import com.groupten.bmsproject.ProductionSchedule.ProductionIngredient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class DisplayProductionScheduleController {

    @FXML
    private TableView<ProductionScheduleEntity> productionScheduleTable;

    @FXML
    private TableColumn<ProductionScheduleEntity, Integer> idColumn;

    @FXML
    private TableColumn<ProductionScheduleEntity, String> productNameColumn;

    @FXML
    private TableColumn<ProductionScheduleEntity, Double> quantityColumn;

    @FXML
    private TableColumn<ProductionScheduleEntity, String> lvlofStockColumn;

    @FXML
    private TableColumn<ProductionScheduleEntity, LocalDate> dateofProductionColumn;

    @FXML
    private TableColumn<ProductionScheduleEntity, LocalDate> expirationDateColumn;

    @FXML
    private TableColumn<ProductionScheduleEntity, Integer> numberOfDaysExpirationColumn;

    @FXML
    private TableView<ProductionIngredient> RecipeTable;

    @FXML
    private TableColumn<ProductionIngredient, String> IngredientNameColumn;

    @FXML
    private TableColumn<ProductionIngredient, Double> QuantityColumn;

    @FXML
    private TableColumn<ProductionIngredient, String> UnitTypeColumn;

    @FXML
    private StackPane editPane;

    @FXML
    private TextField editProductNameField;

    @FXML
    private TextField editQuantityField;

    @FXML
    private TextField editLvlofStockField;

    @FXML
    private DatePicker editDateofProductionField;

    @FXML
    private DatePicker editExpirationDateField;

    @FXML
    private TextField editNumberOfDaysExpirationField;

    @FXML
    private TextField SearchTextfield;

    private final ProductionScheduleService productionScheduleService;
    private ObservableList<ProductionScheduleEntity> productionScheduleList;
    private ProductionScheduleEntity selectedSchedule;
    private ObservableList<ProductionIngredient> recipeData = FXCollections.observableArrayList();

    @Autowired
    public DisplayProductionScheduleController(ProductionScheduleService productionScheduleService) {
        this.productionScheduleService = productionScheduleService;
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productnameProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        lvlofStockColumn.setCellValueFactory(cellData -> cellData.getValue().lvlofstockProperty());
        dateofProductionColumn.setCellValueFactory(cellData -> cellData.getValue().dateofproductionProperty());
        expirationDateColumn.setCellValueFactory(cellData -> cellData.getValue().expdateProperty());
        numberOfDaysExpirationColumn.setCellValueFactory(cellData -> cellData.getValue().numberofdaysexpProperty().asObject());

        IngredientNameColumn.setCellValueFactory(cellData -> cellData.getValue().ingredientProperty());
        QuantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        UnitTypeColumn.setCellValueFactory(cellData -> cellData.getValue().unitTypeProperty());


        // Set cell factory for lvlofStockColumn to add color indicators
        lvlofStockColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item.toLowerCase()) {
                        case "low":
                            setStyle(" -fx-text-fill: red;");
                            break;
                        case "sufficient":
                            setStyle(" -fx-text-fill: green;");
                            break;
                        default:
                            setStyle(""); // Reset to default style for other levels
                            break;
                    }
                }
            }
        });

        populateTable();

        // Add a listener to capture the selected row
        productionScheduleTable.setRowFactory(tv -> {
            TableRow<ProductionScheduleEntity> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                    selectedSchedule = row.getItem();
                    displayIngredients(selectedSchedule);
                }
            });
            return row;
        });

        // Add a listener to the search field to perform search on text change
        SearchTextfield.textProperty().addListener((observable, oldValue, newValue) -> searchSchedules(newValue));
    }

    private void populateTable() {
        List<ProductionScheduleEntity> schedules = productionScheduleService.getAllProducts().stream()
            .filter(schedule -> !"archived".equals(schedule.getStatus()))
            .collect(Collectors.toList());
    
        schedules.forEach(schedule -> {
            if (schedule.getQuantity() <= 5) {
                schedule.setlvlofstock("Low");
            } else {
                schedule.setlvlofstock("Sufficient");
            }
        });
    
        productionScheduleList = FXCollections.observableArrayList(schedules);
        productionScheduleTable.setItems(productionScheduleList);
    }
    

    private void searchSchedules(String query) {
        List<ProductionScheduleEntity> filteredList = productionScheduleList.stream()
                .filter(schedule -> schedule.getProductname().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        productionScheduleTable.setItems(FXCollections.observableArrayList(filteredList));
    }

    private void displayIngredients(ProductionScheduleEntity schedule) {
        recipeData.clear();
        Set<ProductionIngredient> ingredients = productionScheduleService.getIngredientsByProductionScheduleId(schedule.getId());
        recipeData.setAll(ingredients);
        RecipeTable.setItems(recipeData);
    }

    @FXML
    private void handleCancelButton() {
        // Hide the edit pane without saving
        editPane.setVisible(false);
    }

    @FXML
private void handleEditButton() {
    if (selectedSchedule != null) {
        try {
            // Load the EditProductions.fxml and set the controller
            ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditProductions.fxml"));
            loader.setControllerFactory(context::getBean);

            Parent root = loader.load();
            // Get the controller and pass the selected schedule
            EditProductions editProductionsController = loader.getController();
            editProductionsController.setProductionSchedule(selectedSchedule);

            // Show the edit window
            Stage stage = BmsprojectApplication.getPrimaryStage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    } else {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Selection");
        alert.setHeaderText("No Production Schedule Selected");
        alert.setContentText("Please select a production schedule in the table.");
        alert.showAndWait();
    }
}

    @FXML
    private void handleArchiveButton() {
        if (selectedSchedule != null) {
            // Archive the selected schedule
            String result = productionScheduleService.archiveProductionSchedule(selectedSchedule.getId());

            System.out.println(result);
            
            // Remove the schedule from the table
            productionScheduleList.remove(selectedSchedule);
            productionScheduleTable.refresh();

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Production Schedule Selected");
            alert.setContentText("Please select a production schedule in the table.");
            alert.showAndWait();
        }
    }

    @FXML
    private void proceedtoAddProductionSched() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductionSchedReg.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
