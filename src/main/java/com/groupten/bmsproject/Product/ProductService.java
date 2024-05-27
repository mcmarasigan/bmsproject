package com.groupten.bmsproject.Product;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    public String addNewProduct(String productname, String description, Double price, LocalDateTime expiryTime, Integer quantity, String imglocation) {
    
        ProductEntity newProduct = new ProductEntity();
        newProduct.setproductName(productname);
        newProduct.setprodDescription(description);
        newProduct.setprodPrice(price);
        newProduct.setproductExpiry(expiryTime);
        newProduct.setproductQuantity(quantity);
        newProduct.setimageLocation(imglocation);
        productRepository.save(newProduct);

        return "Saved";
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }
}
