package com.groupten.bmsproject.FXMLControllers;

import java.time.LocalDateTime;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import com.groupten.bmsproject.Order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderingController {

    @Autowired
    private OrderService orderService;

    @FXML
    private TextField CustomerNameTextField;
    
    @FXML
    private TextField AddressTextField;
    
    @FXML
    private DatePicker DateOrder;
    
    @FXML
    private TextField ProductOrderTextField;
    
    @FXML
    private TextField QuantityOrderTextField;
    
    @FXML
    private TextField PaymentStatusTextField;
    
    @FXML
    private TextField DeliveryStatusTextField;

    @FXML
    private Button saveOrderButton;

    @FXML
    private void handleSaveButton() {
        String customerName = CustomerNameTextField.getText();
        String address = AddressTextField.getText();
        LocalDateTime dateOrder = DateOrder.getValue().atStartOfDay(); // Convert DatePicker value to LocalDateTime
        String productOrder = ProductOrderTextField.getText();
        Integer quantityOrder = Integer.parseInt(QuantityOrderTextField.getText());
        String paymentStatus = PaymentStatusTextField.getText();
        String deliveryStatus = DeliveryStatusTextField.getText();

        String result = orderService.addNewOrder(customerName, address, dateOrder, productOrder, quantityOrder, paymentStatus, deliveryStatus);
        System.out.println(result);
    }
}
