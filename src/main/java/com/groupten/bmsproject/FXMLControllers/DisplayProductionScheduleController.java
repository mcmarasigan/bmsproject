package com.groupten.bmsproject.FXMLControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.ProductionSchedule.ProductionScheduleEntity;
import com.groupten.bmsproject.ProductionSchedule.ProductionScheduleService;

@Controller
public class DisplayProductionScheduleController {

    @FXML
    private TableView<ProductionScheduleEntity> productionScheduleTable;

    @FXML
    private TableColumn<ProductionScheduleEntity, Integer> idColumn;

    @FXML
    private TableColumn<ProductionScheduleEntity, String> productNameColumn;

    @FXML
    private TableColumn<ProductionScheduleEntity, Integer> quantityColumn;

    @FXML
    private TableColumn<ProductionScheduleEntity, String> lvlofStockColumn;

    @FXML
    private TableColumn<ProductionScheduleEntity, LocalDate> dateofProductionColumn;

    @FXML
    private TableColumn<ProductionScheduleEntity, LocalDate> expirationDateColumn;

    @FXML
    private TableColumn<ProductionScheduleEntity, Integer> numberOfDaysExpirationColumn;

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
    private ObservableList<ProductionScheduleEntity> masterData = FXCollections.observableArrayList();

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

        /* 
        // Disable past dates in the DatePicker
        editDateofProductionField.setDayCellFactory(getDateCellFactory());
        editExpirationDateField.setDayCellFactory(getDateCellFactory());
        */

        populateTable();

        // Add a listener to capture the selected row
        productionScheduleTable.setRowFactory(tv -> {
            TableRow<ProductionScheduleEntity> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                    selectedSchedule = row.getItem();
                }
            });
            return row;
        });

        // Add a listener to the search field to perform search on text change
        SearchTextfield.textProperty().addListener((observable, oldValue, newValue) -> searchSchedules(newValue));
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
        productionScheduleList = FXCollections.observableArrayList(productionScheduleService.getAllProducts());
        // Populate the masterData list with data from the production schedule service
        productionScheduleTable.getItems().addAll(productionScheduleService.getAllProducts());
    }

    private void searchSchedules(String query) {
        List<ProductionScheduleEntity> filteredList = productionScheduleList.stream()
                .filter(schedule -> schedule.getproductName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        productionScheduleTable.setItems(FXCollections.observableArrayList(filteredList));
    }
    /* 
    @FXML
    private void handleEditButton() {
        if (selectedSchedule != null) {
            // Fill the fields with the selected production schedule details
            editProductNameField.setText(selectedSchedule.getproductName());
            editQuantityField.setText(selectedSchedule.getquantity().toString());
            editLvlofStockField.setText(selectedSchedule.getlvlofStock());
            editDateofProductionField.setValue(selectedSchedule.getdateofProduction());
            editExpirationDateField.setValue(selectedSchedule.getexpirationDate());
            editNumberOfDaysExpirationField.setText(selectedSchedule.getnumberOfDaysExpiration().toString());
            // Show the edit pane
            editPane.setVisible(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Schedule Selected");
            alert.setContentText("Please select a Production Schedule in the table.");
            alert.showAndWait();
        }
    }
    @FXML
    private void handleSaveButton() {
        Integer id = selectedSchedule.getId();
        String productName = editProductNameField.getText();
        String lvlofStock = editLvlofStockField.getText();
        LocalDate dateofProduction = editDateofProductionField.getValue();
        LocalDate expirationDate = editExpirationDateField.getValue();
        String quantityString = editQuantityField.getText();
        String numberOfDaysExpirationString = editNumberOfDaysExpirationField.getText();

        int quantity = Integer.parseInt(quantityString);
        int numberOfDaysExpiration = Integer.parseInt(numberOfDaysExpirationString);

        String result = productionScheduleService.updateSchedule(id, productName, lvlofStock, dateofProduction, expirationDate, quantity, numberOfDaysExpiration);

        // Refresh the table to show the updated details
        productionScheduleTable.refresh();

        System.out.println(result);
        
        // Hide the edit pane
        editPane.setVisible(false);
    }
    */

    @FXML
    private void handleCancelButton() {
        // Hide the edit pane without saving
        editPane.setVisible(false);
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
