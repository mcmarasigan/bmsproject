package com.groupten.bmsproject.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groupten.bmsproject.Inventory.InventoryEntity;

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

    public String updateProduct(Integer id, String productname, String description, Double price, LocalDateTime expiryTime, Integer quantity, String imglocation) {
        Optional<ProductEntity> optionalProductEntity = productRepository.findById(id);

        if (optionalProductEntity.isPresent()) {
            // Update existing inventory item
            ProductEntity productEntity = optionalProductEntity.get();
            productEntity.setproductName(productname);
            productEntity.setprodDescription(description);
            productEntity.setprodPrice(price);
            productEntity.setproductExpiry(expiryTime);
            productEntity.setproductQuantity(quantity);
            productEntity.setimageLocation(imglocation);
            productRepository.save(productEntity);
        } 
        return "Updated";
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public List<ProductEntity> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }
}
