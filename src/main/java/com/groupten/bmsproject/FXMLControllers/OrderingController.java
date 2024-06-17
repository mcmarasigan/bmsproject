package com.groupten.bmsproject.FXMLControllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.Parent;
import javafx.scene.Scene;

import com.groupten.bmsproject.BmsprojectApplication;
import com.groupten.bmsproject.Inventory.InventoryEntity;
import com.groupten.bmsproject.Order.OrderEntity.DeliveryStatus;
import com.groupten.bmsproject.Order.OrderEntity.PaymentStatus;
import com.groupten.bmsproject.Order.OrderService;
import com.groupten.bmsproject.Product.ProductService;
import com.groupten.bmsproject.Product.ProductEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class OrderingController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @FXML
    private TextField CustomerNameTextField;

    @FXML
    private TextField AddressTextField;

    @FXML
    private DatePicker DateOrder;

    @FXML
    private ChoiceBox<String> ProductOrderChoiceBox;

    @FXML
    private TextField QuantityOrderTextField;

    @FXML
    private ComboBox<PaymentStatus> PaymentStatusComboBox;

    @FXML
    private ComboBox<DeliveryStatus> DeliveryStatusComboBox;

    @FXML
    private Button SaveOrderButton;

    private static final int LOW_STOCK_THRESHOLD = 5;

    @FXML
    private void initialize() {
        PaymentStatusComboBox.getItems().setAll(PaymentStatus.values());
        DeliveryStatusComboBox.getItems().setAll(DeliveryStatus.values());

        // Disable past dates in the DatePicker
        DateOrder.setDayCellFactory(getDateCellFactory());

        // Populate ProductOrderChoiceBox with available products
        populateProductOrderChoiceBox();
    }

    private void populateProductOrderChoiceBox() {
        List<ProductEntity> availableProducts = productService.getAvailableProducts();
        for (ProductEntity product : availableProducts) {
            ProductOrderChoiceBox.getItems().add(product.getproductName());
        }
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

    @FXML
    private void handleSaveButton() {
        String customerName = CustomerNameTextField.getText();
        String address = AddressTextField.getText();
        LocalDateTime dateOrder = DateOrder.getValue().atStartOfDay(); // Convert DatePicker value to LocalDateTime
        String productOrder = ProductOrderChoiceBox.getValue();
        Integer quantityOrder = Integer.parseInt(QuantityOrderTextField.getText());
        PaymentStatus paymentStatus = PaymentStatusComboBox.getValue();
        DeliveryStatus deliveryStatus = DeliveryStatusComboBox.getValue();

        // Check for low stock products
        List<ProductEntity> lowStockProducts = productService.getLowStockProducts(LOW_STOCK_THRESHOLD);
        if (!lowStockProducts.isEmpty()) {
            StringBuilder warningMessage = new StringBuilder("Warning: Low stock on the following products:\n");
            for (ProductEntity product : lowStockProducts) {
                warningMessage.append(product.getproductName()).append(" (Quantity: ").append(product.productQuantity()).append(")\n");
            }
            
            // Show a warning dialog
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Low Stock Warning");
            alert.setHeaderText("There are low stock products.");
            alert.setContentText(warningMessage.toString());
            alert.showAndWait();
        }

        // Proceed to save the order
        String result = orderService.addNewOrder(customerName, address, dateOrder, productOrder, quantityOrder, paymentStatus, deliveryStatus);
        System.out.println(result);
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
