package com.groupten.bmsproject.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Integer> {

    List<OrderEntity> findByPaymentstatusAndDeliverystatus(OrderEntity.PaymentStatus paymentstatus,
                                                       OrderEntity.DeliveryStatus deliverystatus);

}