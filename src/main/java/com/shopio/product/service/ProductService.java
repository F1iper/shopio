package com.shopio.product.service;

import com.shopio.product.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductService {

    List<Product> getAllProducts();

    Optional<Product> getProductById(String productId);

    Product createProduct(Product product);

    Optional<Product> updateProduct(String productId, Product product);

    void deleteProducts(Set<Product> toDeleteSet);

    List<Product> findByName(String value);

    void deleteProductById(String productId);
}
