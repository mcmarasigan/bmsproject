package com.groupten.bmsproject.Order;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.groupten.bmsproject.Order.OrderEntity.DeliveryStatus;
import com.groupten.bmsproject.Order.OrderEntity.PaymentStatus;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class OrderRegStatusController {
     @Autowired
    private OrderService orderService;

    @PostMapping(path = "/addOrder")
    public String addNewOrder(@RequestParam String customername, @RequestParam String address, @RequestParam LocalDate dateorder,@RequestParam String productorder, @RequestParam Integer quantityorder, @RequestParam PaymentStatus paymentstatus, @RequestParam DeliveryStatus deliverystatus) {
        return orderService.addNewOrder(customername, address, dateorder, productorder, quantityorder, paymentstatus, deliverystatus);
    }
}
