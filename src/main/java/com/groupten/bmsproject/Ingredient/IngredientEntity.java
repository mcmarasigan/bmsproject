package com.groupten.bmsproject.Ingredient;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class IngredientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredient_seq")
    @SequenceGenerator (name = "ingredient_seq", sequenceName = "ingredient_sequence", allocationSize = 1)

    private Integer id;
    private String ingredient;
    private Double price;
    private Double quantity;
    private LocalDate expiryDate;
    private LocalDate lastUpdate;
    private LocalDate dateAdded;
    private String unitType;

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

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExpiry() {
        return expiryDate;
    }

    public void setExpiry(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
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

    public String getUnitType() {
        return unitType;
    }
    
    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

 // Property methods for JavaFX TableView binding
     public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public StringProperty IngredientProperty() {
        return new SimpleStringProperty(ingredient);
    }

     public DoubleProperty priceProperty() {
        return new SimpleDoubleProperty(price);
    }

    public DoubleProperty quantityIngredientProperty() {
        return new SimpleDoubleProperty(quantity);
    }

    public ObjectProperty<LocalDate> expiryDateProperty() {
        return new SimpleObjectProperty<>(expiryDate);
    }
    
    public StringProperty unitTypeProperty() {
        return new SimpleStringProperty(unitType);
    }
}
