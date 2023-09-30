package com.shopio.product.service;

import com.shopio.product.entity.Category;
import com.shopio.product.entity.Product;
import com.shopio.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp(){
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void shouldGetListOfExistingProductsFromDatabase(){
        //given
        List<Product> products = new ArrayList<>();
        products.add(new Product("1", "product1", "description1", 20.22, 200, Category.ELECTRONICS));
        products.add(new Product("2", "product2", "description2", 10.99, 4000, Category.CLOTHING));

        when(productRepository.findAll()).thenReturn(products);

        //when
        List<Product> resultList = productService.getAllProducts();

        //then
        assertEquals(2, resultList.size(), "Result list size is different");
        assertEquals("product1", products.get(0).getName(), "Product name is different");
        assertEquals(10.99, products.get(1).getPrice(), "Price is different");

        verify(productRepository, times(1)).findAll();
    }

    @Test
    void shouldGetProductByIdFromDatabase(){
        //given
        Product product = new Product("3", "product3", "description3", 9.99, 10, Category.BOOKS);

        when(productRepository.findById("3")).thenReturn(Optional.of(product));

        //when
        Optional<Product> result = productService.getProductById("3");

        //then
        assertAll(
                () -> assertTrue(result.isPresent(), "Result should be present"),
                () -> assertEquals("description3", result.get().getDescription(), "Description should match")
        );

        verify(productRepository).findById("3");
    }

    @Test
    void shouldCreateProductWithProperProperties(){
        //given
        Product product = new Product("4", "product4", "description4", 250, 10, Category.ELECTRONICS);

        when(productRepository.save(product)).thenReturn(product);

        //when
        Product result = productService.createProduct(product);

        //then
        assertAll(
                () -> assertEquals(product, result, "Result should match the input product"),
                () -> assertNotNull(result, "Result should not be null"),
                () -> assertNotNull(result.getId(), "Result's ID should not be null or empty")
        );

        verify(productRepository).save(product);
    }

    @Test
    void shouldUpdateProductWithValidProductAndReturnsUpdatedProduct(){
        //given
        String productId = "1";
        Product updatedProduct = new Product(productId, "Updated Product", "Updated Description", 19.99, 5, Category.BOOKS);

        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);

        //when
        Optional<Product> result = productService.updateProduct(productId, updatedProduct);

        //then
        assertTrue(result.isPresent(), "Result should be present");
        assertEquals(updatedProduct, result.get(), "Result should match the updated product");

        verify(productRepository).existsById(productId);
        verify(productRepository).save(updatedProduct);
    }

    @Test
    void shouldDeleteProductWithNonExistentProductIdAndDoesNotDelete(){
        //given
        final String productId = "5";
        when(productRepository.existsById(productId)).thenReturn(false);

        //when
        productService.deleteProduct(productId);

        //then
        verify(productRepository).existsById(productId);
        verify(productRepository, never()).deleteById(productId);
    }
}