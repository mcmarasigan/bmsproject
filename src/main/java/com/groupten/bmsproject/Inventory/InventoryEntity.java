package com.groupten.bmsproject.Inventory;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class InventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inv_seq")
    @SequenceGenerator (name = "inv_seq", sequenceName = "inventory_sequence", allocationSize = 1)

    private Integer id;
    private String ingredient;
    private Double price;
    private Integer quantity;
    private LocalDateTime expiryTime;

    public Integer getID() {
        return id;
    }

    public void setID(Integer id) {
        this.id=id;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getExpiry() {
        return expiryTime;
    }

    public void setExpiry(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

}
