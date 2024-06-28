package com.groupten.bmsproject.Sales;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groupten.bmsproject.Order.OrderEntity;

@Repository
public interface SalesRepository extends JpaRepository<SalesEntity, Integer> {
    List<SalesEntity> findByPaymentStatusAndDeliveryStatus(OrderEntity.PaymentStatus paymentStatus, OrderEntity.DeliveryStatus deliveryStatus);
}
