package com.groupten.bmsproject.Ingredient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class IngredientService {
    
    @Autowired
    private IngredientRepository inventoryRepository;

    public String addNewInventory(String ingredient, Double price, Integer quantity, LocalDate expiryTime, LocalDate dateAdded, String unitType) {
        IngredientEntity newInventory = new IngredientEntity();
        newInventory.setIngredient(ingredient);
        newInventory.setPrice(price);
        newInventory.setQuantity(quantity);
        newInventory.setExpiry(expiryTime);
        newInventory.setDateAdded(dateAdded);
        newInventory.setUnitType(unitType);
        inventoryRepository.save(newInventory);
    
        return "Saved";
    }

    public String updateIngredient(Integer id, String ingredient, Double price, Integer quantity, LocalDate expiryTime, String unitType) {
        Optional<IngredientEntity> optionalInventoryEntity = inventoryRepository.findById(id);
    
        if (optionalInventoryEntity.isPresent()) {
            IngredientEntity inventoryEntity = optionalInventoryEntity.get();
            inventoryEntity.setIngredient(ingredient);
            inventoryEntity.setPrice(price);
            inventoryEntity.setQuantity(quantity);
            inventoryEntity.setExpiry(expiryTime);
            inventoryEntity.setUnitType(unitType);
            inventoryEntity.setLastUpdateTime(LocalDate.now());
            inventoryRepository.save(inventoryEntity);
        }
        return "Updated";
    }

    public List<IngredientEntity> getAllIngredients() {
        List<IngredientEntity> ingredients = inventoryRepository.findAll();
        System.out.println("Fetched ingredients: " + ingredients); // Debug log
        return ingredients;
    }

    public List<IngredientEntity> getAllProducts() {
        return inventoryRepository.findAll();
    }

    public IngredientEntity findByName(String ingredient) {
        return inventoryRepository.findByIngredient(ingredient);
    }

    public IngredientEntity findByName(IngredientEntity ingredient) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByName'");
    }

}
