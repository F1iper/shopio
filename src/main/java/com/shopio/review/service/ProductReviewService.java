package com.shopio.review.service;

import com.shopio.exception.ReviewNotBelongToProductException;
import com.shopio.exception.ReviewNotFoundException;
import com.shopio.review.entity.ProductReview;

import java.util.List;
import java.util.Optional;

public interface ProductReviewService {

    ProductReview createProductReview(Long productId, ProductReview productReview);

    ProductReview updateProductReview(Long productId, Long reviewId, String updateField, String newValue) throws Exception, ReviewNotBelongToProductException, ReviewNotFoundException;

    List<ProductReview> getAllProductReviews();

    Optional<ProductReview> getProductReviewById(Long id);

    void deleteProductReview(Long id);

    List<ProductReview> getReviewsByProductId(Long id);
}