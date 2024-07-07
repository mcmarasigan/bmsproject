package com.groupten.bmsproject.ProductionSchedule;

import com.groupten.bmsproject.FXMLControllers.IngredientTableRow;
import com.groupten.bmsproject.Ingredient.IngredientEntity;
import com.groupten.bmsproject.Ingredient.IngredientService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class ProductionScheduleService {

    @Autowired
    private ProductionScheduleRepository productSchedRepository;

    @Autowired
    private IngredientService ingredientService;

    @PersistenceContext
    private EntityManager entityManager;

    private IngredientTableRow[] ingredients;

    public String addnewProductSched(String productname, Double quantity, String lvlofstock, LocalDate dateofproduction, LocalDate expdate, Integer numberofdaysexp) {
        ProductionScheduleEntity newProductSched = new ProductionScheduleEntity();
        newProductSched.setproductName(productname);
        newProductSched.setproductschedQuantity(quantity);
        newProductSched.setlvlofstock(lvlofstock);
        newProductSched.setdateofProduction(dateofproduction);
        newProductSched.setexpDate(expdate);
        newProductSched.setnumberofdaysexp(numberofdaysexp);
        if (numberofdaysexp <= 0) {
            newProductSched.setExpiryStatus("Expired");
        } else {
            newProductSched.setExpiryStatus("Valid");
        }

        Set<ProductionIngredient> productionIngredients = new HashSet<>();
        for (IngredientTableRow row : ingredients) {
            ProductionIngredient productionIngredient = new ProductionIngredient();
            IngredientEntity ingredient = ingredientService.findByName(row.getIngredientName());
            productionIngredient.setIngredientid(ingredient);
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

    public ProductionScheduleEntity getProductionScheduleByName(String productname) {
        return productSchedRepository.findByProductname(productname);
    }

    public ProductionScheduleEntity getProductionScheduleById(Integer id) {
        return productSchedRepository.findById(id).orElse(null);
    }

    public void save(ProductionScheduleEntity productionSchedule) {
        productSchedRepository.save(productionSchedule);
    }

    public void saveProductionIngredient(ProductionIngredient productionIngredient) {
        productSchedRepository.save(productionIngredient);
    }

    public void updateProductQuantity(int id, Double newQuantity) {
        ProductionScheduleEntity productionScheduleEntity = productSchedRepository.findById(id).orElse(null);
        if (productionScheduleEntity != null) {
            productionScheduleEntity.setproductschedQuantity(newQuantity);
            productSchedRepository.save(productionScheduleEntity);
        }
    }

    public void updateProductionSchedule(ProductionScheduleEntity productionSchedule) {
        productSchedRepository.save(productionSchedule);
    }

    public Set<ProductionIngredient> getIngredientsByProductionScheduleId(Integer id) {
        ProductionScheduleEntity productionSchedule = entityManager.find(ProductionScheduleEntity.class, id);
        if (productionSchedule != null) {
            // Initialize the ingredients collection
            productionSchedule.getIngredient().size();
            return productionSchedule.getIngredient();
        }
        return new HashSet<>();
    }

    // Archive a production schedule
    public String archiveProductionSchedule(Integer scheduleId) {
        // Retrieve the schedule by ID
        ProductionScheduleEntity schedule = productSchedRepository.findById(scheduleId).orElse(null);

        if (schedule == null) {
            return "Production schedule not found.";
        }

        // Set the schedule's status to "archived"
        schedule.setStatus("archived");
        productSchedRepository.save(schedule);

        return "Production schedule archived successfully.";
    }

    // Archive a production schedule
    public String removeArchiveProductionSchedule(Integer scheduleId) {
        // Retrieve the schedule by ID
        ProductionScheduleEntity schedule = productSchedRepository.findById(scheduleId).orElse(null);

        if (schedule == null) {
            return "Production schedule not found.";
        }

        // Set the schedule's status to "archived"
        schedule.setStatus("active");
        productSchedRepository.save(schedule);

        return "Production removed from archived successfully.";
    }

    public void removeIngredientsByProductionScheduleId(Integer scheduleId) {
        ProductionScheduleEntity schedule = productSchedRepository.findById(scheduleId).orElse(null);
        if (schedule != null) {
            schedule.getIngredient().clear();
            productSchedRepository.save(schedule);
        }
    }

    // Check and update expiry status
    public void checkAndUpdateExpiryStatus() {
        List<ProductionScheduleEntity> schedules = productSchedRepository.findAll();
        LocalDate today = LocalDate.now();

        for (ProductionScheduleEntity schedule : schedules) {
            if (schedule.getExpdate().isBefore(today)) {
                schedule.setExpiryStatus("expired");
            } else {
                schedule.setExpiryStatus("active");
            }
            productSchedRepository.save(schedule);
        }
    }
}
