package com.groupten.bmsproject.ProductionSchedule;

import com.groupten.bmsproject.Ingredient.IngredientEntity;
import jakarta.persistence.*;
import javafx.beans.property.*;

@Entity
public class ProductionIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "production_schedule_id")
    private ProductionScheduleEntity productionSchedule;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ingredient_id")
    private IngredientEntity ingredient;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private String unitType;

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public ProductionScheduleEntity getProductionschedule() {
        return productionSchedule;
    }
    public Integer getProductionscheduleid() {
        return productionSchedule != null ? productionSchedule.getId() : null;
    }

    public void setProductionschedule(ProductionScheduleEntity productionSchedule) {
        this.productionSchedule = productionSchedule;
    }

    public IngredientEntity getIngredientid() {
        return ingredient;
    }

    public void setIngredientid(IngredientEntity ingredient) {
        this.ingredient = ingredient;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
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

    public StringProperty ingredientProperty() {
        return ingredient.IngredientProperty();
    }

    public DoubleProperty quantityProperty() {
        return new SimpleDoubleProperty(quantity);
    }

    public StringProperty unitTypeProperty() {
        return new SimpleStringProperty(unitType);
    }
}
