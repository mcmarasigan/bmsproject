package com.groupten.bmsproject.Product;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator (name = "product_seq", sequenceName = "product_sequence", allocationSize = 1)

    private Integer id;
    private String productname;
    private String description;
    private Double price;
    private LocalDateTime expiryTime;
    private Integer quantity;
    private String imglocation;

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

    public LocalDateTime productExpiry() {
        return expiryTime;
    }

    public void setproductExpiry(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public Integer productQuantity() {
        return quantity;
    }

    public void setproductQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String imageLocation() {
        return imglocation;
    }

    public void setimageLocation(String imglocation) {
        this.imglocation = imglocation;
    }
}
