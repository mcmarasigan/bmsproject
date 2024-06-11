package com.groupten.bmsproject.FXMLControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.groupten.bmsproject.ProductionSchedule.ProductionScheduleEntity;
import com.groupten.bmsproject.ProductionSchedule.ProductionScheduleService;
import java.time.LocalDateTime;

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
    private TableColumn<ProductionScheduleEntity, LocalDateTime> dateofProductionColumn;

    @FXML
    private TableColumn<ProductionScheduleEntity, LocalDateTime> expirationDateColumn;

    @FXML
    private TableColumn<ProductionScheduleEntity, Integer> numberOfDaysExpirationColumn;

    @FXML
    private TextField SearchTextfield;

    private final ProductionScheduleService productionScheduleService;
    private final ObservableList<ProductionScheduleEntity> masterData = FXCollections.observableArrayList();

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

        // Populate the masterData list with data from the production schedule service
        masterData.addAll(productionScheduleService.getAllProducts());

        // Wrap the ObservableList in a FilteredList
        FilteredList<ProductionScheduleEntity> filteredData = new FilteredList<>(masterData, p -> true);

        // Add a listener to the searchTextfield to filter the data
        SearchTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(productionScheduleEntity -> {
                // If the search field is empty, display all production schedules
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare production schedule details with the filter text
                String lowerCaseFilter = newValue.toLowerCase();
                if (productionScheduleEntity.getproductName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product name
                }
                // You can add more conditions here for other columns if needed

                return false; // Does not match
            });
        });

        // Bind the FilteredList to the TableView
        productionScheduleTable.setItems(filteredData);
    }
}
