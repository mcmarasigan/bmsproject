package com.groupten.bmsproject.Order;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groupten.bmsproject.Inventory.InventoryEntity;
import com.groupten.bmsproject.Order.OrderEntity.DeliveryStatus;
import com.groupten.bmsproject.Order.OrderEntity.PaymentStatus;



@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public String addNewOrder(String customername, String address, LocalDateTime dateorder,String productorder, Integer quantityorder, PaymentStatus paymentstatus, DeliveryStatus deliverystatus) {
    
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

    public String updateOrder(Integer id, String customername, String address, LocalDateTime dateorder,String productorder, Integer quantityorder, PaymentStatus paymentstatus, DeliveryStatus deliverystatus) {
        Optional<OrderEntity> optionalOrderEntity = orderRepository.findById(id);

        if (optionalOrderEntity.isPresent()) {
            // Update existing inventory item
            OrderEntity orderEntity = optionalOrderEntity.get();
            orderEntity.setorderCustomerName(customername);
            orderEntity.setorderaddress(address);
            orderEntity.setorderDateOrder (dateorder);
            orderEntity.setorderProductOrder (productorder);
            orderEntity.setorderQuantityOrder (quantityorder);
            orderEntity.setorderPaymentStatus (paymentstatus);
            orderEntity.setorderDeliveryStatus (deliverystatus);
            orderRepository.save(orderEntity);
        } 
        return "Updated";
    }

    public List<OrderEntity> getAllProducts() {
        return orderRepository.findAll();
    }
}
