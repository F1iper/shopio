package com.shopio.review.service.impl;

import com.shopio.exception.ReviewNotBelongToProductException;
import com.shopio.exception.ReviewNotFoundException;
import com.shopio.review.entity.ProductReview;
import com.shopio.review.repository.ProductReviewRepository;
import com.shopio.review.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductReviewServiceImpl implements ProductReviewService {
    private final ProductReviewRepository productReviewRepository;

    @Override
    public ProductReview createProductReview(String productId, ProductReview productReview){
        try {
            productReview.setProductId(productId);
            ProductReview createdReview = productReviewRepository.save(productReview);

            if (createdReview != null) {
                return createdReview;
            } else {
                throw new RuntimeException("Failed to create product review.");
            }
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while creating the product review.", e);
        }
    }

    @Override
    public ProductReview updateProductReview(String productId, String reviewId, String updateField, String newValue){
        try {
            Optional<ProductReview> optionalProductReview = productReviewRepository.findById(reviewId);
            if (optionalProductReview.isPresent()) {
                ProductReview existingReview = optionalProductReview.get();

                if (! productId.equals(existingReview.getProductId())) {
                    throw new ReviewNotBelongToProductException("Review with ID [" + reviewId + "] does not belong to specific product with ID [" + productId + "].");
                }
                if ("comment".equalsIgnoreCase(updateField)) {
                    existingReview.setComment(newValue);
                } else if ("rating".equalsIgnoreCase(updateField)) {
                    existingReview.setRating(Integer.parseInt(newValue));
                } else {
                    throw new IllegalArgumentException("Invalid updateField: " + updateField);
                }

                ProductReview updatedReview = productReviewRepository.save(existingReview);
                log.info("Updated {} for review ID [{}].", updateField, reviewId);
                return updatedReview;
            } else {
                throw new ReviewNotFoundException("Review with ID: [" + reviewId + "] not found.");
            }
        } catch (ReviewNotBelongToProductException | ReviewNotFoundException exception) {
            log.error("Error updating product review.", exception);
        }
        throw new RuntimeException("An unexpected error occurred.");
    }

    @Override
    public List<ProductReview> getAllProductReviews(){
        return productReviewRepository.findAll();
    }

    @Override
    public Optional<ProductReview> getProductReviewById(String id){
        return productReviewRepository.findById(id);
    }

    @Override
    public void deleteProductReview(String id){
        if (productReviewRepository.existsById(id)) {
            productReviewRepository.deleteById(id);
        }
    }
}
