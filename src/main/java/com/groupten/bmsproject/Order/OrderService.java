package com.groupten.bmsproject.Order;


import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public String addNewOrder(String customername, String address, LocalDateTime dateorder,String productorder, Integer quantityorder, String paymentstatus, String deliverystatus) {
    
    OrderEntity newOrder = new OrderEntity();
    newOrder.setorderCustomerName(customername);
    newOrder.setorderaddress(address);
    newOrder.setorderDateOrder (dateorder);
    newOrder.setorderProductOrder (productorder);
    newOrder.setorderQuantityOrder (quantityorder);
    newOrder.setorderPaymentStatus (paymentstatus);
    newOrder.setorderDeliveryStatus (deliverystatus);
    orderRepository.save(newOrder);

    return "Saved";
    }
    public List<OrderEntity> getAllProducts() {
        return orderRepository.findAll();
    }
}
