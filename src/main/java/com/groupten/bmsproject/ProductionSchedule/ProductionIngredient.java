package com.groupten.bmsproject.ProductionSchedule;

import com.groupten.bmsproject.Ingredient.IngredientEntity;
import jakarta.persistence.*;

@Entity
public class ProductionIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_schedule_id")
    private ProductionScheduleEntity productionSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private IngredientEntity ingredient;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String unitType;

    // Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProductionScheduleEntity getProductionSchedule() {
        return productionSchedule;
    }

    public void setProductionSchedule(ProductionScheduleEntity productionSchedule) {
        this.productionSchedule = productionSchedule;
    }

    public IngredientEntity getIngredient() {
        return ingredient;
    }

    public void setIngredient(IngredientEntity ingredient) {
        this.ingredient = ingredient;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }
}
