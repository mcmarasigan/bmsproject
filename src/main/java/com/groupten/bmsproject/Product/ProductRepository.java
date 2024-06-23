package com.groupten.bmsproject.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    /*List<ProductEntity> findByQuantityGreaterThan(Integer quantity);
    List<ProductEntity> findByQuantityLessThanEqual(Integer quantity);*/
    ProductEntity findByProductname(String productname);
}