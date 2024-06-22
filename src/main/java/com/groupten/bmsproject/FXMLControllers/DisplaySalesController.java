package com.groupten.bmsproject.FXMLControllers;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Ingredient.IngredientEntity;
import com.groupten.bmsproject.Sales.SalesEntity;
import com.groupten.bmsproject.Sales.SalesService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@Controller
public class DisplaySalesController {

    @FXML
    private TableView<SalesEntity> SalesTable;

    @FXML
    private TableColumn<SalesEntity, Integer> SalesIdColumn;

    @FXML
    private TableColumn<SalesEntity, String> CustomerNameSalesColumn;

    @FXML
    private TableColumn<SalesEntity, LocalDate> DatePurchasedSalesColumn;

    @FXML
    private TableColumn<SalesEntity, String> ProductOrderSalesColumn;

    @FXML
    private TableColumn<SalesEntity, Integer> QuantityOrderSalesColumn;

    @FXML
    private TableColumn<SalesEntity, Double> ProductPriceSalesColumn;

    @FXML
    private TableColumn<SalesEntity, Double> TotalAmountSalesColumn;

    @FXML
    private TextField SearchSaleTextField;

    private SalesService salesService;

    private  ObservableList<SalesEntity> paidDeliveredOrders;

    @Autowired
    public DisplaySalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @FXML
    public void initialize() {
        // Initialize columns
        SalesIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        CustomerNameSalesColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        DatePurchasedSalesColumn.setCellValueFactory(cellData -> cellData.getValue().datePurchasedProperty());
        ProductOrderSalesColumn.setCellValueFactory(cellData -> cellData.getValue().productOrderProperty());
        QuantityOrderSalesColumn.setCellValueFactory(cellData -> cellData.getValue().quantityOrderProperty().asObject());
        ProductPriceSalesColumn.setCellValueFactory(cellData -> cellData.getValue().productPriceProperty().asObject());
        TotalAmountSalesColumn.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty().asObject());

    
        // Load data into table
        populateTable();
    }

    private void populateTable() {
        // Fetch data from service
        paidDeliveredOrders = FXCollections.observableArrayList(salesService.getPaidAndDeliveredOrders());
        // Populate table with fetched data
        SalesTable.setItems(paidDeliveredOrders);
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
