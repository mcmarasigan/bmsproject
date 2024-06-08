package com.groupten.bmsproject.FXMLControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Order.OrderEntity;
import com.groupten.bmsproject.Order.OrderService;

@Controller
public class DisplayOrderController {

    @FXML
    private TableView<OrderEntity> OrderTable;

    @FXML
    private TableColumn<OrderEntity, Integer> idColumn;

    @FXML
    private TableColumn<OrderEntity, String> CustomerNameColumn;

    @FXML
    private TableColumn<OrderEntity, String> AddressColumn;

    @FXML
    private TableColumn<OrderEntity, LocalDateTime> DateOrderColumn;

    @FXML
    private TableColumn<OrderEntity, String> ProductOrderColumn;

    @FXML
    private TableColumn<OrderEntity, Integer> QuantityColumn;

    @FXML
    private TableColumn<OrderEntity, String> PaymentColumn;

    @FXML
    private TableColumn<OrderEntity, String> DeliveryColumn;

    @FXML
    private TextField SearchTextField;

    private final OrderService orderService;
    private ObservableList<OrderEntity> masterData = FXCollections.observableArrayList();

    @Autowired
    public DisplayOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        CustomerNameColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        AddressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        DateOrderColumn.setCellValueFactory(cellData -> cellData.getValue().dateOrderProperty());
        ProductOrderColumn.setCellValueFactory(cellData -> cellData.getValue().productOrderProperty());
        QuantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityOrderProperty().asObject());
        PaymentColumn.setCellValueFactory(cellData -> cellData.getValue().paymentStatusProperty());
        DeliveryColumn.setCellValueFactory(cellData -> cellData.getValue().deliveryStatusProperty());

        populateTable();

    }
    
        private void populateTable() {
        // Populate the masterData list with data from the order service
        OrderTable.getItems().addAll(orderService.getAllProducts());
        }
        

    @FXML
    private void proceedtoAddOrders() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/OrderStatus.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void backtoOrders() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Orders.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();
        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
