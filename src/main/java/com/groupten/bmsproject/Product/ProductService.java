package com.groupten.bmsproject.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public String addNewProduct(String productname, String description, Double price, LocalDate expiryTime, Integer quantity, String imglocation, LocalDate dateAdded) {
        ProductEntity newProduct = new ProductEntity();
        newProduct.setproductName(productname);
        newProduct.setprodDescription(description);
        newProduct.setprodPrice(price);
        newProduct.setproductExpiry(expiryTime);
        newProduct.setproductQuantity(quantity);
        newProduct.setimageLocation(imglocation);
        newProduct.setDateAdded(dateAdded);
        productRepository.save(newProduct);
        return "Saved";
    }

    public String updateProduct(Integer id, String productname, String description, Double price, LocalDate expiryTime, Integer quantity, String imglocation) {
        Optional<ProductEntity> optionalProductEntity = productRepository.findById(id);
        if (optionalProductEntity.isPresent()) {
            ProductEntity productEntity = optionalProductEntity.get();
            productEntity.setproductName(productname);
            productEntity.setprodDescription(description);
            productEntity.setprodPrice(price);
            productEntity.setproductExpiry(expiryTime);
            productEntity.setproductQuantity(quantity);
            productEntity.setimageLocation(imglocation);
            productEntity.setLastUpdateTime(LocalDate.now());
            productRepository.save(productEntity);
        }
        return "Updated";
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    public List<ProductEntity> getAvailableProducts() {
        return productRepository.findByQuantityGreaterThan(0);
    }

    public List<ProductEntity> getLowStockProducts(int threshold) {
        return productRepository.findByQuantityLessThanEqual(threshold);
    }
    public ProductEntity getProductByName(String productname) {
        return productRepository.findByProductname(productname);
    }
}
