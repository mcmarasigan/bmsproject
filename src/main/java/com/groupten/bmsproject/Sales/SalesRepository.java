package com.groupten.bmsproject.Sales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.groupten.bmsproject.Order.OrderEntity;

import java.util.List;

@Repository
public interface SalesRepository extends JpaRepository<OrderEntity, Integer> {
    
    List<OrderEntity> findByPaymentstatusAndDeliverystatus(OrderEntity.PaymentStatus paymentstatus,
                                                           OrderEntity.DeliveryStatus deliverystatus);
}
