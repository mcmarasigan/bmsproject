package com.groupten.bmsproject.ProductionSchedule;
import com.groupten.bmsproject.FXMLControllers.IngredientTableRow;
import com.groupten.bmsproject.Ingredient.IngredientEntity;
import com.groupten.bmsproject.Ingredient.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.time.LocalDate;

import com.groupten.bmsproject.Ingredient.IngredientService;

@Service
public class ProductionScheduleService {

    @Autowired
    private ProductionScheduleRepository productSchedRepository;

    @Autowired
    private IngredientService ingredientService;

    private IngredientTableRow[] ingredients;

    public String addnewProductSched(String productname, Integer quantity, String lvlofstock, LocalDate dateofproduction, LocalDate expdate,  Integer numberofdaysexp) {
        ProductionScheduleEntity newProductSched = new ProductionScheduleEntity();
        newProductSched.setproductName(productname);
        newProductSched.setproductschedQuantity(quantity);
        newProductSched.setlvlofstock(lvlofstock);
        newProductSched.setdateofProduction(dateofproduction);
        newProductSched.setexpDate(expdate);
        newProductSched.setnumberofdaysexp(numberofdaysexp);

        Set<ProductionIngredient> productionIngredients = new HashSet<>();
        for (IngredientTableRow row : ingredients) {
            ProductionIngredient productionIngredient = new ProductionIngredient();
            IngredientEntity ingredient = ingredientService.findByName(row.getIngredientName());
            productionIngredient.setIngredient(ingredient);
            productionIngredient.setQuantity(row.getQuantity());
            productionIngredient.setUnitType(row.getUnitType());
            productionIngredient.setProductionSchedule(newProductSched);
            productionIngredients.add(productionIngredient);
        }

        newProductSched.setIngredients(productionIngredients);

        productSchedRepository.save(newProductSched);
        return "Saved";
    }

    public List<ProductionScheduleEntity> getAllProducts() {
        return productSchedRepository.findAll();
    }
    public void save(ProductionScheduleEntity productionSchedule) {
        productSchedRepository.save(productionSchedule);
    }

    public void saveProductionIngredient(ProductionIngredient productionIngredient) {
        productSchedRepository.save(productionIngredient);
    }
}
