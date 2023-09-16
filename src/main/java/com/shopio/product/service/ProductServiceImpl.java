package com.shopio.product.service;

import com.shopio.product.entity.Product;
import com.shopio.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(String productId){
        return productRepository.findById(productId);
    }

    @Override
    public Product createProduct(Product product){
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> updateProduct(String productId, Product product){
        if (! productRepository.existsById(productId)) {
            return Optional.empty();
        }
        product.setId(productId);
        return Optional.of(productRepository.save(product));
    }

    public void deleteProduct(String productId){
        productRepository.deleteById(productId);
    }
}