package com.shopio.product.repository;

import com.shopio.product.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByNameRegexIgnoreCase(String value);
}
