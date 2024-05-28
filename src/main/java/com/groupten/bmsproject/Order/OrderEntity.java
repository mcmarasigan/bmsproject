package com.groupten.bmsproject.Order;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_seq")
    @SequenceGenerator (name = "order_seq", sequenceName = "order_sequence", allocationSize = 1)

    private Integer id;
    private String customername;
    private String address;
    private LocalDateTime dateorder;
    private String productorder;
    private Integer quantityorder;
    private String paymentstatus;
    private String deliverystatus;

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public String getorderCustomerName() {
        return customername;
    }

    public void setorderCustomerName(String customername) {
        this.customername = customername;
    }

    public String getorderAddress() {
        return address;
    }

    public void setorderaddress(String address) {
        this.address = address;
    }

    public LocalDateTime getorderDateOrder() {
        return dateorder;
    }

    public void setorderDateOrder(LocalDateTime dateorder) {
        this.dateorder = dateorder;
    }

    public String getorderProductOrder() {
        return productorder;
    }

    public void setorderProductOrder(String productorder) {
        this.productorder = productorder;
    }

    public Integer getorderQuantityOrder() {
        return quantityorder;
    }

    public void setorderQuantityOrder(Integer quantityorder) {
        this.quantityorder = quantityorder;
    }

    public String getorderPaymentStatus() {
        return paymentstatus;
    }

    public void setorderPaymentStatus(String paymentstatus) {
        this.paymentstatus = paymentstatus;
    }

    public String getorderDeliveryStatus() {
        return deliverystatus;
    }

    public void setorderDeliveryStatus(String deliverystatus) {
        this.deliverystatus = deliverystatus;
    }
     // Property methods for JavaFX TableView binding
     public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public StringProperty customerNameProperty() {
        return new SimpleStringProperty(customername);
    }

    public StringProperty addressProperty() {
        return new SimpleStringProperty(address);
    }

    public ObjectProperty<LocalDateTime> dateOrderProperty() {
        return new SimpleObjectProperty<>(dateorder);
    }

    public StringProperty productOrderProperty() {
        return new SimpleStringProperty(productorder);
    } 

    public IntegerProperty quantityOrderProperty() {
        return new SimpleIntegerProperty(quantityorder);
    }

    public StringProperty paymentStatusProperty() {
        return new SimpleStringProperty(paymentstatus);
    }

    public StringProperty deliveryStatusProperty() {
        return new SimpleStringProperty(deliverystatus);
    }

}
