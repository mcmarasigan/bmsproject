package com.groupten.bmsproject.Product;

import java.time.LocalDate;

import com.groupten.bmsproject.Order.OrderEntity.PaymentStatus;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator (name = "product_seq", sequenceName = "product_sequence", allocationSize = 1)

    private Integer id;
    private String productname;
    private String description;
    private Double price;
    private String imglocation;
    private LocalDate lastUpdate;
    private LocalDate dateAdded;

    @Enumerated(EnumType.STRING)
    private QuantityType quantitytype;

    // Enums:
    public enum QuantityType {
        PCS,
        PANS
    }

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public String getproductName() {
        return productname;
    }

    public void setproductName(String productname) {
        this.productname = productname;
    }

    public QuantityType getQuantityType() {
        return quantitytype;
    }

    public void setQuantityType(QuantityType quantitytype) {
        this.quantitytype = quantitytype;
    }

    public String prodDescription() {
        return description;
    }

    public void setprodDescription(String description) {
        this.description = description;
    }

    public Double price() {
        return price;
    }

    public void setprodPrice(Double price) {
        this.price = price;
    }

    public String imageLocation() {
        return imglocation;
    }

    public void setimageLocation(String imglocation) {
        this.imglocation = imglocation;
    }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdateTime(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDate dateAdded) {
        this.dateAdded = dateAdded;
    }
     // Property methods for JavaFX TableView binding
     public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public StringProperty productnameProperty() {
        return new SimpleStringProperty(productname);
    }

    public StringProperty descriptionProperty() {
        return new SimpleStringProperty(description);
    }

    public DoubleProperty priceProperty() {
        return new SimpleDoubleProperty(price);
    }

    public ObjectProperty<QuantityType> quantityTypeProperty() {
        return new SimpleObjectProperty(quantitytype);
    }

    public StringProperty imglocationProperty() {
        return new SimpleStringProperty(imglocation);
    }

}
