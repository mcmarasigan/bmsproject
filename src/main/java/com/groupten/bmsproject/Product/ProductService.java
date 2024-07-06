package com.groupten.bmsproject.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.groupten.bmsproject.Product.ProductEntity.QuantityType;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public String addNewProduct(String productname, String description, Double price, QuantityType quantitytype, String imglocation, LocalDate dateAdded) {
        // Check if a product with the same name already exists
        if (productRepository.findByProductname(productname) != null) {
            return "Product with the same name already exists.";
        }

        ProductEntity newProduct = new ProductEntity();
        newProduct.setproductName(productname);
        newProduct.setprodDescription(description);
        newProduct.setprodPrice(price);
        newProduct.setQuantityType(quantitytype);
        newProduct.setimageLocation(imglocation);
        newProduct.setDateAdded(dateAdded);
        newProduct.setStatus("active");
        productRepository.save(newProduct);
        return "Saved";
    }

    public String updateProduct(Integer id, String productname, String description, Double price, QuantityType quantitytype, String imglocation) {
        Optional<ProductEntity> optionalProductEntity = productRepository.findById(id);
        if (optionalProductEntity.isPresent()) {
            ProductEntity productEntity = optionalProductEntity.get();
            productEntity.setproductName(productname);
            productEntity.setprodDescription(description);
            productEntity.setprodPrice(price);
            productEntity.setQuantityType(quantitytype);
            productEntity.setimageLocation(imglocation);
            productEntity.setLastUpdateTime(LocalDate.now());
            productRepository.save(productEntity);
        }
        return "Updated";
    }

    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    // Get product with ID
    public ProductEntity getProductByID(int id) {
        Optional<ProductEntity> product = productRepository.findById(id);
        return product.orElse(null);
    }

    // Get product with name
    public ProductEntity getProductByName(String productname) {
        return productRepository.findByProductname(productname);
    }

    // Archive products
    public String archiveProduct(Integer productId) {
        // Retrieve the product by ID
        ProductEntity product = productRepository.findById(productId).orElse(null);
    
        if (product == null) {
            return "Product not found.";
        }
    
        // Set the product's status to "archived"
        product.setStatus("archived");
        productRepository.save(product);
    
        return "Product archived successfully.";
    }

    // Remove Archived products
    public String removeArchivedProduct(Integer productId) {
        // Retrieve the product by ID
        ProductEntity product = productRepository.findById(productId).orElse(null);
    
        if (product == null) {
            return "Product not found.";
        }
    
        // Set the product's status to "active"
        product.setStatus("active");
        productRepository.save(product);
    
        return "Product removed from archived successfully.";
    }

    public List<ProductEntity> getActiveProducts() {
        return productRepository.findAll().stream()
                .filter(product -> !product.getStatus().equalsIgnoreCase("archived"))
                .collect(Collectors.toList());
    }
}
