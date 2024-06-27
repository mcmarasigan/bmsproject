package com.groupten.bmsproject.ProductionSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionScheduleRepository extends JpaRepository<ProductionScheduleEntity, Integer> {
    void save(ProductionIngredient productionIngredient);
    ProductionScheduleEntity findByProductname(String productname);
    
}
