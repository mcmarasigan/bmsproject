package com.groupten.bmsproject.ProductionSchedule;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductionScheduleService {

    @Autowired
    private ProductionScheduleRepository productSchedRepository;

    public String addnewProductSched(String productname, Integer quantity, String lvlofstock, LocalDate dateofproduction, LocalDate expdate,  Integer numberofdaysexp) {
        ProductionScheduleEntity newProductSched = new ProductionScheduleEntity();
        newProductSched.setproductName(productname);
        newProductSched.setproductschedQuantity(quantity);
        newProductSched.setlvlofstock(lvlofstock);
        newProductSched.setdateofProduction(dateofproduction);
        newProductSched.setexpDate(expdate);
        newProductSched.setnumberofdaysexp(numberofdaysexp);
        productSchedRepository.save(newProductSched);
        return "Saved";
    }

    public List<ProductionScheduleEntity> getAllProducts() {
        return productSchedRepository.findAll();
    }
    public ProductionScheduleEntity save(ProductionScheduleEntity productionSchedule) {
        return productSchedRepository.save(productionSchedule);
    }
}
