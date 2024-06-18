package com.groupten.bmsproject.Product;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(path = "/addProduct")
    public String addNewProduct(@RequestParam String productname, @RequestParam String description, @RequestParam Double price, @RequestParam LocalDate expiryTime, @RequestParam Integer quantity, @RequestParam String imglocation, @RequestParam LocalDate dateAdded) {
        return productService.addNewProduct(productname, description, price, expiryTime, quantity, imglocation, dateAdded);
    }
    
}
