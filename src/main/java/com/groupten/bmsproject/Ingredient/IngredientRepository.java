package com.groupten.bmsproject.Ingredient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<IngredientEntity, Integer>{

    IngredientEntity findByIngredient(String ingredient);

}
