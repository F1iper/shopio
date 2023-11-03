package com.shopio.product.repository;

import com.shopio.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE %:value%")
    List<Product> findByNameRegexIgnoreCase(String value);
}
