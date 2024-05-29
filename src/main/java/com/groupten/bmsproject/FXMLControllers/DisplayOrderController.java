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

        // Populate the masterData list with data from the order service
        masterData.addAll(orderService.getAllProducts());

        // Wrap the ObservableList in a FilteredList
        FilteredList<OrderEntity> filteredData = new FilteredList<>(masterData, p -> true);

        // Add a listener to the SearchTextField to filter the data
        SearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(order -> {
                // If the search field is empty, display all orders
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare order details with the filter text
                String lowerCaseFilter = newValue.toLowerCase();

                if (order.getorderCustomerName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches customer name
                } else if (order.getorderAddress().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches address
                } else if (order.getorderProductOrder().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches product order
                } else if (order.getorderPaymentStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches payment status
                } else if (order.getorderDeliveryStatus().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches delivery status
                }
                return false; // Does not match
            });
        });

        // Bind the FilteredList to the TableView
        OrderTable.setItems(filteredData);
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
