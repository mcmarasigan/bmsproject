package com.groupten.bmsproject.Order;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groupten.bmsproject.Ingredient.IngredientEntity;
import com.groupten.bmsproject.Order.OrderEntity.DeliveryStatus;
import com.groupten.bmsproject.Order.OrderEntity.PaymentStatus;



@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public String addNewOrder(String customername, String address, LocalDate dateorder,String productorder, Integer quantityorder, PaymentStatus paymentstatus, DeliveryStatus deliverystatus) {
    
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

    public String updateOrder(Integer id, String customername, String address, LocalDate dateorder,String productorder, Integer quantityorder, PaymentStatus paymentstatus, DeliveryStatus deliverystatus) {
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

    // Archive order
    public String archiveOrder(Integer orderId) {
        // Retrieve the order by ID
        OrderEntity order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return "Order not found.";
        }

        // Set the order's status to "archived"
        order.setStatus("archived");
        orderRepository.save(order);

        return "Order archived successfully.";
    }

    public String removeArchivedOrder(Integer orderId) {
        // Retrieve the order by ID
        OrderEntity order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return "Order not found.";
        }

        // Set the order's status to "active"
        order.setStatus("active");
        orderRepository.save(order);

        return "Order removed from archived successfully.";
    }
}
