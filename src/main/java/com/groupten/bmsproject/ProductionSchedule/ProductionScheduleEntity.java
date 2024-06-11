package com.groupten.bmsproject.ProductionSchedule;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
public class ProductionScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "productsched_seq")
    @SequenceGenerator (name = "productsched_seq", sequenceName = "productsched_sequence", allocationSize = 1)

    private Integer id;
    private String productname;
    private Integer quantity;
    private String lvlofstock;
    private LocalDateTime dateofproduction;
    private LocalDateTime expdate;
    private Integer numberofdaysexp;

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

    public Integer getproductschedQuantity() {
        return quantity;
    }

    public void setproductschedQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getlvlofstock() {
        return lvlofstock;
    }

    public void setlvlofstock(String lvlofstock) {
        this.lvlofstock = lvlofstock;
    }

    public LocalDateTime getdateofProduction() {
        return dateofproduction;
    }

    public void setdateofProduction(LocalDateTime dateofproduction) {
        this.dateofproduction = dateofproduction;
    }

    public LocalDateTime getexpDate() {
        return expdate;
    }

    public void setexpDate(LocalDateTime expdate) {
        this.expdate = expdate;
    }

    public Integer getnumberofdaysexp() {
        return numberofdaysexp;
    }

    public void setnumberofdaysexp(Integer numberofdaysexp) {
        this.numberofdaysexp = numberofdaysexp;
    }

     // Property methods for JavaFX TableView binding
     public IntegerProperty idProperty() {
        return new SimpleIntegerProperty(id);
    }

    public StringProperty productnameProperty() {
        return new SimpleStringProperty(productname);
    }

    public IntegerProperty quantityProperty() {
        return new SimpleIntegerProperty(quantity);
    }

    public StringProperty lvlofstockProperty() {
        return new SimpleStringProperty(lvlofstock);
    }

    public ObjectProperty<LocalDateTime> dateofproductionProperty() {
        return new SimpleObjectProperty<>(dateofproduction);
    }

    public ObjectProperty<LocalDateTime> expdateProperty() {
        return new SimpleObjectProperty<>(expdate);
    }

    public IntegerProperty numberofdaysexpProperty() {
        // Calculate the number of days until expiration
        long daysUntilExpiration = ChronoUnit.DAYS.between(dateofproduction, expdate);
        return new SimpleIntegerProperty((int) daysUntilExpiration);
    }
}
