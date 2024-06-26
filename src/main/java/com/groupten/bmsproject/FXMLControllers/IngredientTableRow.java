package com.groupten.bmsproject.FXMLControllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class IngredientTableRow {
    private final SimpleStringProperty ingredient;
    private final SimpleIntegerProperty quantity;
    private final SimpleStringProperty unitType;

    public IngredientTableRow(String ingredient, int quantity, String unitType) {
        this.ingredient = new SimpleStringProperty(ingredient);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.unitType = new SimpleStringProperty(unitType);
    }

    public String getIngredientName() {
        return ingredient.get();
    }

    public SimpleStringProperty ingredientNameProperty() {
        return ingredient;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public String getUnitType() {
        return unitType.get();
    }

    public SimpleStringProperty unitTypeProperty() {
        return unitType;
    }
}
