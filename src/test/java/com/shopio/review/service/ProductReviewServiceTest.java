package com.shopio.review.service;

import com.shopio.exception.ReviewNotBelongToProductException;
import com.shopio.exception.ReviewNotFoundException;
import com.shopio.product.entity.Category;
import com.shopio.product.entity.Product;
import com.shopio.product.repository.ProductRepository;
import com.shopio.review.entity.ProductReview;
import com.shopio.review.repository.ProductReviewRepository;
import com.shopio.review.service.impl.ProductReviewServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductReviewServiceTest {

    public static final Long productId = 1L;
    public static final Long reviewId = 111L;
    public static final Long userId = 999L;
    public static final String initialComment = "initial review";
    public static final String initialRating = "3";
    public static final String updatedComment = "updated review";
    public static final String updatedRating = "4";

    @InjectMocks
    private ProductReviewServiceImpl productReviewService;

    @Mock
    private ProductReviewRepository productReviewRepository;
    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateProductReviewWithExistingProduct(){
        //given
        Product product = new Product(productId, "smartphone", "smart phone", 800, 5, Category.ELECTRONICS);
        ProductReview productReview = new ProductReview(reviewId, product.getId(), userId, initialComment, initialRating);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productReviewRepository.save(productReview)).thenReturn(productReview);

        //when
        ProductReview createdReview = productReviewService.createProductReview(productId, productReview);

        //then
        assertAll(
                () -> assertEquals(productId, createdReview.getProductId()),
                () -> assertEquals(userId, createdReview.getUserId())
        );

        verify(productReviewRepository).save(any());
    }

    @Test
    void shouldNotCreateProductReviewWithNonExistingProductAndThrowException(){
        //given
        Product product = new Product(2L, "smartphone", "smart phone", 800, 5, Category.ELECTRONICS);
        ProductReview productReview = new ProductReview(reviewId, product.getId(), userId, initialComment, initialRating);

        when(productRepository.findById(2L)).thenReturn(Optional.empty());
        when(productReviewRepository.save(productReview)).thenReturn(productReview);

        //when and then
        assertThrows(RuntimeException.class, () -> {
            productReviewService.createProductReview(userId, productReview);
        });

        verify(productReviewRepository, never()).save(any());
    }

    @Test
    void shouldUpdateProductReviewComment() throws ReviewNotFoundException, ReviewNotBelongToProductException, Exception{
        //given
        String updateField = "comment";
        ProductReview existingReview = new ProductReview(reviewId, productId, userId, initialComment, initialRating);
        ProductReview updatedReview = new ProductReview(reviewId, productId, userId, updatedComment, initialRating);

        when(productReviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(productReviewRepository.save(updatedReview)).thenReturn(updatedReview);

        //when
        ProductReview result = productReviewService.updateProductReview(productId, reviewId, updateField, updatedComment);

        //then
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(updatedComment, result.getComment()),
                () -> assertEquals(initialRating, result.getRating())
        );

        verify(productReviewRepository).findById(reviewId);
        verify(productReviewRepository).save(updatedReview);
    }

    @Test
    void shouldUpdateProductReviewRating() throws ReviewNotFoundException, ReviewNotBelongToProductException, Exception{
        //given
        String updateField = "rating";
        ProductReview existingReview = new ProductReview(reviewId, productId, userId, initialComment, initialRating);
        ProductReview updatedReview = new ProductReview(reviewId, productId, userId, initialComment, updatedRating);

        when(productReviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(productReviewRepository.save(updatedReview)).thenReturn(updatedReview);

        //when
        ProductReview result = productReviewService.updateProductReview(productId, reviewId, updateField, updatedRating);

        //then
        assertNotNull(result);
        assertEquals(initialComment, result.getComment());
        assertEquals(updatedRating, result.getRating());

        verify(productReviewRepository).findById(reviewId);
        verify(productReviewRepository).save(updatedReview);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingInvalidField(){
        //given
        String updateField = "invalidField";
        String newValue = "updated value";

        when(productReviewRepository.findById(reviewId)).thenReturn(Optional.of(new ProductReview(reviewId, productId, userId, initialComment, initialRating)));

        //when and then
        assertThrows(IllegalArgumentException.class, () -> {
            productReviewService.updateProductReview(productId, reviewId, updateField, newValue);
        });
    }

    @Test
    void getAllProductReviews(){
        //given
        List<ProductReview> productReviews = new ArrayList<>();
        productReviews.add(new ProductReview(reviewId, productId, userId, initialComment, "5"));
        productReviews.add(new ProductReview(reviewId + 1, productId, userId + 1, initialComment, "4"));

        when(productReviewRepository.findAll()).thenReturn(productReviews);

        //when
        List<ProductReview> result = productReviewService.getAllProductReviews();

        //then
        assertEquals(2, result.size());

        verify(productReviewRepository, times(1)).findAll();
    }

    @Test
    void getProductReviewById(){
        //given
        ProductReview expectedReview = new ProductReview(reviewId, productId, userId, initialComment, initialRating);

        when(productReviewRepository.findById(reviewId)).thenReturn(Optional.of(expectedReview));

        //when
        Optional<ProductReview> resultProductReview = productReviewService.getProductReviewById(reviewId);

        //then
        assertAll(
                () -> assertTrue(resultProductReview.isPresent()),
                () -> assertEquals(expectedReview, resultProductReview.get())
        );

        verify(productReviewRepository, times(1)).findById(reviewId);
    }

    @Test
    void deleteProductReview(){
        //given
        ProductReview expectedReview = new ProductReview(reviewId, productId, userId, initialComment, initialRating);

        when(productReviewRepository.existsById(reviewId)).thenReturn(true);
        when(productReviewRepository.findById(reviewId)).thenReturn(Optional.of(expectedReview));

        //when
        productReviewService.deleteProductReview(reviewId);

        //then
        verify(productReviewRepository, times(1)).existsById(reviewId);
        verify(productReviewRepository, times(1)).deleteById(reviewId);
    }
}