package com.groupten.bmsproject.FXMLControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Ingredient.IngredientEntity;
import com.groupten.bmsproject.Order.OrderEntity;
import com.groupten.bmsproject.Order.OrderEntity.DeliveryStatus;
import com.groupten.bmsproject.Order.OrderEntity.PaymentStatus;
import com.groupten.bmsproject.Order.OrderService;

@Controller
public class DisplayArchivedOrders {

    @FXML
    private TableView<OrderEntity> OrderTable;

    @FXML
    private TableColumn<OrderEntity, Integer> idColumn;

    @FXML
    private TableColumn<OrderEntity, String> CustomerNameColumn;

    @FXML
    private TableColumn<OrderEntity, String> AddressColumn;

    @FXML
    private TableColumn<OrderEntity, LocalDate> DateOrderColumn;

    @FXML
    private TableColumn<OrderEntity, String> ProductOrderColumn;

    @FXML
    private TableColumn<OrderEntity, Integer> QuantityColumn;

    @FXML
    private TableColumn<OrderEntity, PaymentStatus> PaymentColumn;

    @FXML
    private TableColumn<OrderEntity, DeliveryStatus> DeliveryColumn;

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
    private ComboBox<PaymentStatus> editPaymentfield;

    @FXML
    private ComboBox<DeliveryStatus> editDeliveryfield;

    @FXML
    private TextField SearchTextfield;

    private final OrderService orderService;
    private ObservableList<OrderEntity> orderList;
    private OrderEntity selectedOrder;
    private ObservableList<OrderEntity> masterData = FXCollections.observableArrayList();

    @Autowired
    public DisplayArchivedOrders(OrderService orderService) {
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

        // Initialize ComboBoxes for editing
        editPaymentfield.setItems(FXCollections.observableArrayList(PaymentStatus.values()));
        editDeliveryfield.setItems(FXCollections.observableArrayList(DeliveryStatus.values()));

        // Disable past dates in the DatePicker
        editDateorderedfield.setDayCellFactory(getDateCellFactory());


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
        orderList = FXCollections.observableArrayList(
            orderService.getAllProducts().stream()
                .filter(order -> "archived".equalsIgnoreCase(order.getStatus()))
                .collect(Collectors.toList())
        );
        OrderTable.setItems(orderList);
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
            editDateorderedfield.setValue(selectedOrder.getorderDateOrder());
            editOrderfield.setText(selectedOrder.getorderProductOrder());
            editQuantityfield.setText(selectedOrder.getorderQuantityOrder().toString());
            editPaymentfield.setValue(selectedOrder.getorderPaymentStatus());
            editDeliveryfield.setValue(selectedOrder.getorderDeliveryStatus());
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
        PaymentStatus paymentstatus = editPaymentfield.getValue();
        DeliveryStatus deliverystatus = editDeliveryfield.getValue();
        // TBA: Double price = Double.parseDouble(priceString);

        int quantity = Integer.parseInt(quantityString);

        // Retrieve the selected date from the DatePicker
        LocalDate orderDate = editDateorderedfield.getValue();

        String result = orderService.updateOrder(id, customer, address, orderDate, order, quantity, paymentstatus, deliverystatus);

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
private void handleArchiveOrderButton() {
    if (selectedOrder != null) {
        // Archive the selected order
        String result = orderService.removeArchivedOrder(selectedOrder.getID());

        System.out.println(result);

        // Remove the order from the table
        orderList.remove(selectedOrder);
        OrderTable.refresh();

        // Hide the edit pane if it's visible
        editPane.setVisible(false);
    } else {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Selection");
        alert.setHeaderText("No Order Selected");
        alert.setContentText("Please select an order in the table.");
        alert.showAndWait();
    }
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

    //Retrives the search text from the Ingredient search then 
    public void setSearchTextField(String result) {
        SearchTextfield.setText(result);
    }

    //Sets the selected Order as the row retrieved from the order search
    public void setSelectedOrder(OrderEntity selectedOrder) {
        this.selectedOrder = selectedOrder;
        displaySelectedOrder();
    }

    //Displays the selected row
    private void displaySelectedOrder() {
        // Set the table's items to only the selected order
        OrderTable.setItems(FXCollections.observableArrayList(selectedOrder));
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
