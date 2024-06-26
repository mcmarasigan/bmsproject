package com.groupten.bmsproject.ProductionSchedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionIngredientRepository extends JpaRepository<ProductionIngredient, Integer> {
}
