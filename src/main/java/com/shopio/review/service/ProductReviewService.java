package com.shopio.review.service;

import com.shopio.exception.ReviewNotBelongToProductException;
import com.shopio.exception.ReviewNotFoundException;
import com.shopio.review.entity.ProductReview;

import java.util.List;
import java.util.Optional;

public interface ProductReviewService {

    ProductReview createProductReview(String productId, ProductReview productReview);

    ProductReview updateProductReview(String productId, String reviewId, String updateField, String newValue) throws Exception, ReviewNotBelongToProductException, ReviewNotFoundException;

    List<ProductReview> getAllProductReviews();

    Optional<ProductReview> getProductReviewById(String id);

    void deleteProductReview(String id);

    List<ProductReview> getReviewsByProductId(String id);
}