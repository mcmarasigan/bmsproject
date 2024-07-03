package com.groupten.bmsproject.ProductionSchedule;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionScheduleRepository extends JpaRepository<ProductionScheduleEntity, Integer> {
    void save(ProductionIngredient productionIngredient);
    ProductionScheduleEntity findByProductname(String productname);
    Optional<ProductionScheduleEntity> findById(Integer id);
    
}
