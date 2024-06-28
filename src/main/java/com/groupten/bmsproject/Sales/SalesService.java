package com.groupten.bmsproject.Sales;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groupten.bmsproject.Order.OrderEntity;
import com.groupten.bmsproject.Order.OrderRepository;
import com.groupten.bmsproject.Product.ProductEntity;
import com.groupten.bmsproject.Product.ProductRepository;

@Service
public class SalesService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final SalesRepository salesRepository;

    @Autowired
    public SalesService(OrderRepository orderRepository, ProductRepository productRepository, SalesRepository salesRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.salesRepository = salesRepository;
    }

    public List<SalesEntity> getPaidAndDeliveredOrders() {
        // Fetch orders with PAID payment status and DELIVERED delivery status
        List<OrderEntity> paidAndDeliveredOrders = orderRepository.findAll()
                .stream()
                .filter(order -> order.getorderPaymentStatus() == OrderEntity.PaymentStatus.PAID
                                && order.getorderDeliveryStatus() == OrderEntity.DeliveryStatus.DELIVERED)
                .collect(Collectors.toList());

        // Convert orders to sales entities
        List<SalesEntity> salesEntities = convertToSalesEntities(paidAndDeliveredOrders);

        // Save sales entities to the database
        salesRepository.saveAll(salesEntities);

        return salesEntities;
    }

    private List<SalesEntity> convertToSalesEntities(List<OrderEntity> orders) {
        return orders.stream()
                .map(order -> {
                    SalesEntity salesEntity = new SalesEntity();
                    salesEntity.setId(order.getID());
                    salesEntity.setCustomerName(order.getorderCustomerName());
                    salesEntity.setDatePurchased(order.getorderDateOrder());
                    salesEntity.setProductOrder(order.getorderProductOrder());
                    salesEntity.setQuantityOrder(order.getorderQuantityOrder());
                    salesEntity.setPaymentStatus(order.getorderPaymentStatus());
                    salesEntity.setDeliveryStatus(order.getorderDeliveryStatus());

                    // Fetch product price from ProductEntity
                    Double productPrice = getProductPrice(order.getorderProductOrder());
                    salesEntity.setProductPrice(productPrice);

                    // Calculate total amount
                    Double totalAmount = productPrice * order.getorderQuantityOrder();
                    salesEntity.setTotalAmount(totalAmount);

                    return salesEntity;
                })
                .collect(Collectors.toList());
    }

    private Double getProductPrice(String productName) {
        ProductEntity product = productRepository.findByProductname(productName);
        if (product != null) {
            return product.price();
        }
        return 0.0; // Or handle appropriately if product not found
    }
}
