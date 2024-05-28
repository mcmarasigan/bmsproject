package com.groupten.bmsproject.Inventory;

import java.time.LocalDateTime;

import java.util.List;
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
    public List<InventoryEntity> getAllProducts() {
        return inventoryRepository.findAll();
    }
}
