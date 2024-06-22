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

    public String addNewInventory(String ingredient, Double price, Integer quantity, LocalDate expiryTime, LocalDate dateAdded) {
        IngredientEntity newInventory = new IngredientEntity();
        newInventory.setIngredient(ingredient);
        newInventory.setPrice(price);
        newInventory.setQuantity(quantity);
        newInventory.setExpiry(expiryTime);
        newInventory.setDateAdded(dateAdded);
        inventoryRepository.save(newInventory);

        return "Saved";
    }

    public String updateIngredient(Integer id, String ingredient, Double price, Integer quantity, LocalDate expiryTime) {
        Optional<IngredientEntity> optionalInventoryEntity = inventoryRepository.findById(id);

        if (optionalInventoryEntity.isPresent()) {
            // Update existing inventory item
            IngredientEntity inventoryEntity = optionalInventoryEntity.get();
            inventoryEntity.setIngredient(ingredient);
            inventoryEntity.setPrice(price);
            inventoryEntity.setQuantity(quantity);
            inventoryEntity.setExpiry(expiryTime);
            inventoryEntity.setLastUpdateTime(LocalDate.now()); // Set the last update time
            inventoryRepository.save(inventoryEntity);
        } 
        return "Updated";
    }

    public List<IngredientEntity> getAllProducts() {
        return inventoryRepository.findAll();
    }
}
