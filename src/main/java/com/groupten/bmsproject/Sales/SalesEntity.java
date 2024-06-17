package com.groupten.bmsproject.Sales;

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
public class SalesEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sales_seq")
    @SequenceGenerator(name = "sales_seq", sequenceName = "sales_sequence", allocationSize = 1)
    private Integer id;

    private String customerName;
    private LocalDateTime datePurchased;
    private String productOrder;
    private Integer quantityOrder;
    private Double productPrice;
    private Double totalAmount;

    // Constructors
    public SalesEntity() {
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getDatePurchased() {
        return datePurchased;
    }

    public void setDatePurchased(LocalDateTime datePurchased) {
        this.datePurchased = datePurchased;
    }

    public String getProductOrder() {
        return productOrder;
    }

    public void setProductOrder(String productOrder) {
        this.productOrder = productOrder;
    }

    public Integer getQuantityOrder() {
        return quantityOrder;
    }

    public void setQuantityOrder(Integer quantityOrder) {
        this.quantityOrder = quantityOrder;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    // Property methods for JavaFX TableView binding
    public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public StringProperty customerNameProperty() {
        return new SimpleStringProperty(customerName);
    }

    public ObjectProperty<LocalDateTime> datePurchasedProperty() {
        return new SimpleObjectProperty<>(datePurchased);
    }

    public StringProperty productOrderProperty() {
        return new SimpleStringProperty(productOrder);
    }

    public IntegerProperty quantityOrderProperty() {
        return new SimpleIntegerProperty(quantityOrder);
    }

    public DoubleProperty productPriceProperty() {
        return new SimpleDoubleProperty(productPrice);
    }

    public DoubleProperty totalAmountProperty() {
        return new SimpleDoubleProperty(totalAmount);
    }

}
