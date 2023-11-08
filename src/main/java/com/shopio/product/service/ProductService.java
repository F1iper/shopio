package com.shopio.product.service;

import com.shopio.product.entity.Product;
import com.vaadin.flow.component.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductService {

    List<Product> getAllProducts();

    Optional<Product> getProductById(Long productId);

    Product createProduct(Product product);

    Optional<Product> updateProduct(Long productId, Product product);

    void deleteProducts(Set<Product> toDeleteSet);

    List<Product> findByName(String value);

    void deleteProductById(Long productId);

    long countProducts();
}
