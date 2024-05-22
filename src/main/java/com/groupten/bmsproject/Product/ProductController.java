package com.groupten.bmsproject.Product;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(path = "/add")
    public String addNewProduct(@RequestParam String productname, @RequestParam String description, @RequestParam Double price, @RequestParam LocalDateTime expiryTime, @RequestParam Integer quantity, @RequestParam String imglocation) {
        return productService.addNewProduct(productname, description, price, expiryTime, quantity, imglocation);
    }
    
}
