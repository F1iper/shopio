package com.shopio.product.service;

import com.shopio.product.entity.Product;
import com.shopio.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long productId){
        return productRepository.findById(productId);
    }

    @Override
    public Product createProduct(Product product){
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> updateProduct(Long productId, Product product){
        if (! productRepository.existsById(productId)) {
            return Optional.empty();
        }
        product.setId(productId);
        return Optional.of(productRepository.save(product));
    }

    public void deleteProducts(Set<Product> toDeleteSet){
        productRepository.deleteAll(toDeleteSet);
    }

    @Override
    public List<Product> findByName(String value){
        return productRepository.findByNameRegexIgnoreCase(value);
    }

    @Override
    public void deleteProductById(Long productId){
        productRepository.deleteById(productId);
    }
}