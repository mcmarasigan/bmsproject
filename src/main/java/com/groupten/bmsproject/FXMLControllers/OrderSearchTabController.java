package com.groupten.bmsproject.FXMLControllers;

import java.io.IOException;
import java.time.LocalDateTime;
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
import com.groupten.bmsproject.Order.OrderEntity;
import com.groupten.bmsproject.Order.OrderEntity.DeliveryStatus;
import com.groupten.bmsproject.Order.OrderEntity.PaymentStatus;
import com.groupten.bmsproject.Order.OrderService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

@Controller
public class OrderSearchTabController {

    @FXML
    private TableView<OrderEntity> ordersTable;

    @FXML
    private TableColumn<OrderEntity, Integer> orderID;

    @FXML
    private TableColumn<OrderEntity, String> customerName;

    @FXML
    private TableColumn<OrderEntity, String> orderAddress;

    @FXML
    private TableColumn<OrderEntity, LocalDateTime> orderDate;

    @FXML
    private TableColumn<OrderEntity, String> productOrder;

    @FXML
    private TableColumn<OrderEntity, Integer> orderQuantity;

    @FXML
    private TableColumn<OrderEntity, PaymentStatus> orderPaymentstatus;

    @FXML
    private TableColumn<OrderEntity, DeliveryStatus> orderDeliverystatus;

    private OrderEntity selectedOrder;

    @FXML
    private TextField searchField;

    private final OrderService orderService;

    private ObservableList<OrderEntity> OrderList;
    

    @Autowired
    public OrderSearchTabController(OrderService orderService) {
        this.orderService = orderService;
    }

    @FXML
    private void initialize() {
        orderID.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        customerName.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
        orderAddress.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        orderDate.setCellValueFactory(cellData -> cellData.getValue().dateOrderProperty());
        productOrder.setCellValueFactory(cellData -> cellData.getValue().productOrderProperty());
        orderQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityOrderProperty().asObject());
        orderPaymentstatus.setCellValueFactory(cellData -> cellData.getValue().paymentStatusProperty());
        orderDeliverystatus.setCellValueFactory(cellData -> cellData.getValue().deliveryStatusProperty());

        populateTable();

        // Add a listener to capture the selected row
        ordersTable.setRowFactory(tv -> {
            TableRow<OrderEntity> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                    selectedOrder = row.getItem();
                    try {
                        proceedtoOrder();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });

        // Add a listener to the search field to perform search on text change
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchOrders(newValue));

    }

    private void populateTable() {
        // Initialize OrderList with data from OrderService
        OrderList = FXCollections.observableArrayList(orderService.getAllProducts());
        ordersTable.getItems().addAll(orderService.getAllProducts());
    }

    private void searchOrders(String query) {
        List<OrderEntity> filteredList = OrderList.stream()
                .filter(order -> order.getorderCustomerName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        ordersTable.setItems(FXCollections.observableArrayList(filteredList));
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
    private void proceedtoOrder() throws IOException {
        ConfigurableApplicationContext context = BmsprojectApplication.getApplicationContext(); // Get the application context
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DisplayOrder.fxml"));
        loader.setControllerFactory(context::getBean);

        Parent root = loader.load();

        // Get the controller and set the search text
        DisplayOrderController controller = loader.getController();
        controller.setSearchTextField(getsearchText());
        controller.setSelectedOrder(selectedOrder);

        Stage stage = BmsprojectApplication.getPrimaryStage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
