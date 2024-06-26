package com.groupten.bmsproject.FXMLControllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class IngredientTableRow {
    private final SimpleStringProperty ingredient;
    private final SimpleDoubleProperty quantity;
    private final SimpleStringProperty unitType;

    public IngredientTableRow(String ingredient, Double quantity, String unitType) {
        this.ingredient = new SimpleStringProperty(ingredient);
        this.quantity = new SimpleDoubleProperty(quantity);
        this.unitType = new SimpleStringProperty(unitType);
    }

    public String getIngredientName() {
        return ingredient.get();
    }

    public SimpleStringProperty ingredientNameProperty() {
        return ingredient;
    }

    public Double getQuantity() {
        return quantity.get();
    }

    public SimpleDoubleProperty quantityProperty() {
        return quantity;
    }

    public String getUnitType() {
        return unitType.get();
    }

    public SimpleStringProperty unitTypeProperty() {
        return unitType;
    }
}
