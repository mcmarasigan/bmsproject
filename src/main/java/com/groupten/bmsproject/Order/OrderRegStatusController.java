package com.groupten.bmsproject.Order;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class OrderRegStatusController {
     @Autowired
    private OrderService orderService;

    @PostMapping(path = "/addOrder")
    public String addNewOrder(@RequestParam String customername, @RequestParam String address, @RequestParam LocalDateTime dateorder,@RequestParam String productorder, @RequestParam Integer quantityorder, @RequestParam String paymentstatus, @RequestParam String deliverystatus) {
        return orderService.addNewOrder(customername, address, dateorder, productorder, quantityorder, paymentstatus, deliverystatus);
    }
}
