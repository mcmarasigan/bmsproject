package com.groupten.bmsproject.Ingredient;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class IngredientController {
    
    @Autowired
    private IngredientService ingredientService;

    @PostMapping(path = "/addIngredient")
    public String addNewIngredient(@RequestParam String ingredient, @RequestParam Double price, @RequestParam Double quantity, @RequestParam LocalDate expiryTime, @RequestParam LocalDate dateAdded, @RequestParam String unitType) {
        return ingredientService.addNewIngredient(ingredient, price, quantity, expiryTime, dateAdded, unitType);
    }
    
}
