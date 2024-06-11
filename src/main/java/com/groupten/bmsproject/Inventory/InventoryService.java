package com.groupten.bmsproject.Inventory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class InventoryService {
    
    @Autowired
    private InventoryRepository inventoryRepository;

    public String addNewInventory(String ingredient, Double price, Integer quantity, LocalDateTime expiryTime) {
        InventoryEntity newInventory = new InventoryEntity();
        newInventory.setIngredient(ingredient);
        newInventory.setPrice(price);
        newInventory.setQuantity(quantity);
        newInventory.setExpiry(expiryTime);
        inventoryRepository.save(newInventory);

        return "Saved";
    }

    public String updateIngredient(Integer id, String ingredient, Double price, Integer quantity, LocalDateTime expiryTime) {
        Optional<InventoryEntity> optionalInventoryEntity = inventoryRepository.findById(id);

        if (optionalInventoryEntity.isPresent()) {
            // Update existing inventory item
            InventoryEntity inventoryEntity = optionalInventoryEntity.get();
            inventoryEntity.setIngredient(ingredient);
            inventoryEntity.setPrice(price);
            inventoryEntity.setQuantity(quantity);
            inventoryEntity.setExpiry(expiryTime);
            inventoryRepository.save(inventoryEntity);
        } 
        return "Updated";
    }

    public List<InventoryEntity> getAllProducts() {
        return inventoryRepository.findAll();
    }
}
