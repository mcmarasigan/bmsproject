package com.groupten.bmsproject.Ingredient;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientService {

    @Autowired
    private IngredientRepository ingredientRepository;

    public String addNewIngredient(String ingredient, Double price, Double quantity, LocalDate expiryTime, LocalDate dateAdded, String unitType) {
        IngredientEntity newIngredient = new IngredientEntity();
        newIngredient.setIngredient(ingredient);
        newIngredient.setPrice(price);
        newIngredient.setQuantity(quantity);
        newIngredient.setExpiry(expiryTime);
        newIngredient.setDateAdded(dateAdded);
        newIngredient.setUnitType(unitType);
        
        // Calculate number of days until expiration and set the value
        long daysUntilExpiration = ChronoUnit.DAYS.between(LocalDate.now(), expiryTime);
        newIngredient.setNumberOfDaysExp((int) daysUntilExpiration);

        // Set expiry status based on the current date and expiry date
        String expiryStatus = !expiryTime.isAfter(LocalDate.now()) ? "Expired" : "Valid";
        newIngredient.setExpiryStatus(expiryStatus);

        ingredientRepository.save(newIngredient);
    
        return "Saved";
    }

    public String updateIngredient(Integer id, String ingredient, Double price, Double quantity, LocalDate expiryTime, String unitType) {
        Optional<IngredientEntity> optionalIngredientEntity = ingredientRepository.findById(id);
    
        if (optionalIngredientEntity.isPresent()) {
            IngredientEntity ingredientEntity = optionalIngredientEntity.get();
            ingredientEntity.setIngredient(ingredient);
            ingredientEntity.setPrice(price);
            ingredientEntity.setQuantity(quantity);
            ingredientEntity.setExpiry(expiryTime);
            ingredientEntity.setUnitType(unitType);
            ingredientEntity.setLastUpdateTime(LocalDate.now());

            // Update number of days until expiration
            long daysUntilExpiration = ChronoUnit.DAYS.between(LocalDate.now(), expiryTime);
            ingredientEntity.setNumberOfDaysExp((int) daysUntilExpiration);

            // Update expiry status based on the current date and expiry date
            String expiryStatus = !expiryTime.isAfter(LocalDate.now()) ? "Expired" : "Valid";
            ingredientEntity.setExpiryStatus(expiryStatus);

            ingredientRepository.save(ingredientEntity);
        }
        return "Updated";
    }

    public List<IngredientEntity> getAllIngredients() {
        List<IngredientEntity> ingredients = ingredientRepository.findAll();
        System.out.println("Fetched ingredients: " + ingredients); // Debug log
        return ingredients;
    }

    public List<IngredientEntity> getAllProducts() {
        return ingredientRepository.findAll();
    }

    public IngredientEntity findByName(String ingredient) {
        return ingredientRepository.findByIngredient(ingredient);
    }

    public IngredientEntity findByName(IngredientEntity ingredient) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByName'");
    }

    public void saveIngredient(IngredientEntity ingredient) {
        // Update number of days until expiration
        long daysUntilExpiration = ChronoUnit.DAYS.between(ingredient.getDateAdded(), ingredient.getExpiryDate());
        ingredient.setNumberOfDaysExp((int) daysUntilExpiration);

        // Update expiry status based on the number of days until expiration
        String expiryStatus = daysUntilExpiration <= 0 ? "Expired" : "Valid";
        ingredient.setExpiryStatus(expiryStatus);

        ingredientRepository.save(ingredient);
    }

    // Archive ingredient
    public String archiveIngredient(Integer ingredientId) {
        // Retrieve the ingredient by ID
        IngredientEntity ingredient = ingredientRepository.findById(ingredientId).orElse(null);

        if (ingredient == null) {
            return "Ingredient not found.";
        }

        // Set the ingredient's status to "archived"
        ingredient.setStatus("archived");
        ingredientRepository.save(ingredient);

        return "Ingredient archived successfully.";
    }

    public String removeArchivedIngredient(Integer ingredientId) {
        // Retrieve the ingredient by ID
        IngredientEntity ingredient = ingredientRepository.findById(ingredientId).orElse(null);
    
        if (ingredient == null) {
            return "Ingredient not found.";
        }
    
        // Set the ingredient's status to "active"
        ingredient.setStatus("active");
        ingredientRepository.save(ingredient);
    
        return "Ingredient removed from archived successfully.";
    }

    public void updateExpiryAndDaysUntilExpiration(IngredientEntity ingredient) {
        long daysUntilExpiration = ChronoUnit.DAYS.between(LocalDate.now(), ingredient.getExpiryDate());
        ingredient.setNumberOfDaysExp((int) daysUntilExpiration);
        String expiryStatus = !ingredient.getExpiryDate().isAfter(LocalDate.now()) ? "Expired" : "Valid";
        ingredient.setExpiryStatus(expiryStatus);
        ingredientRepository.save(ingredient);
    }

    public void updateAllIngredients() {
        List<IngredientEntity> ingredients = ingredientRepository.findAll();
        for (IngredientEntity ingredient : ingredients) {
            updateExpiryAndDaysUntilExpiration(ingredient);
        }
    }
}
