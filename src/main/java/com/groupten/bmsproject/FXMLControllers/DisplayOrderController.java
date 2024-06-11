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
    private StackPane editPane;

    @FXML
    private TextField editCustomerfield;

    @FXML
    private TextField editAddressfield;

    @FXML
    private DatePicker editDateorderedfield;

    @FXML
    private TextField editOrderfield;

    @FXML
    private TextField editQuantityfield;

    @FXML
    private TextField editPaymentfield;

    @FXML
    private TextField editDeliveryfield;

    private OrderEntity selectedOrder;

    @FXML
    private TextField SearchTextfield;

    private final OrderService orderService;
    private ObservableList<OrderEntity> orderList;
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

        // Add a listener to capture the selected row
        OrderTable.setRowFactory(tv -> {
            TableRow<OrderEntity> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {
                    selectedOrder = row.getItem();
                }
            });
            return row;
        });

         // Add a listener to the search field to perform search on text change
         SearchTextfield.textProperty().addListener((observable, oldValue, newValue) -> searchOrders(newValue));

    }
    
        private void populateTable() {
        orderList = FXCollections.observableArrayList(orderService.getAllProducts());
        // Populate the masterData list with data from the order service
        OrderTable.getItems().addAll(orderService.getAllProducts());
        }

        private void searchOrders(String query) {
            List<OrderEntity> filteredList = orderList.stream()
                    .filter(order -> order.getorderCustomerName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
                OrderTable.setItems(FXCollections.observableArrayList(filteredList));
        }

        @FXML
    private void handleEditButton() {
        if (selectedOrder != null) {
            // Fill the fields with the selected ingredient details
            editCustomerfield.setText(selectedOrder.getorderCustomerName());
            editAddressfield.setText(selectedOrder.getorderAddress());
            editDateorderedfield.setValue(selectedOrder.getorderDateOrder().toLocalDate());
            editOrderfield.setText(selectedOrder.getorderProductOrder());
            editQuantityfield.setText(selectedOrder.getorderQuantityOrder().toString());
            editPaymentfield.setText(selectedOrder.getorderPaymentStatus());
            editDeliveryfield.setText(selectedOrder.getorderDeliveryStatus());
            // Show the edit pane
            editPane.setVisible(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Order Selected");
            alert.setContentText("Please select an Order in the table.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleSaveButton() {
        Integer id = selectedOrder.getID();
        String customer = editCustomerfield.getText();
        String address = editAddressfield.getText();
        String order = editOrderfield.getText();
        String quantityString = editQuantityfield.getText();
        String paymentstatus = editPaymentfield.getText();
        String deliverystatus = editDeliveryfield.getText();
        // TBA: Double price = Double.parseDouble(priceString);

        int quantity = Integer.parseInt(quantityString);

        // Retrieve the selected date from the DatePicker
        LocalDate orderDate = editDateorderedfield.getValue();

        // Set the expiry time to the selected date at midnight
        LocalDateTime dateordered = orderDate.atStartOfDay();

        String result = orderService.updateOrder(id, customer, address, dateordered, order, quantity, paymentstatus, deliverystatus);

        // Refresh the table to show the updated details
        OrderTable.refresh();

        System.out.println(result);
        
        // Hide the edit pane
        editPane.setVisible(false);
    }

    @FXML
    private void handleCancelButton() {
        // Hide the edit pane without saving
        editPane.setVisible(false);
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
