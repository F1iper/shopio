package com.shopio.review.service.impl;

import com.shopio.exception.ReviewNotBelongToProductException;
import com.shopio.exception.ReviewNotFoundException;
import com.shopio.product.entity.Product;
import com.shopio.product.repository.ProductRepository;
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
class ProductReviewServiceImpl implements ProductReviewService {
    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;

    @Override
    public ProductReview createProductReview(String productId, ProductReview productReview){
        try {
            Product existingProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product with ID: " + productId + " does not exist"));

            productReview.setProductId(existingProduct.getId());
            ProductReview createdReview = productReviewRepository.save(productReview);

            log.info("Created a new review with ID [{}] for product [{}].", createdReview.getId(), productId);

            return createdReview;
        } catch (Exception e) {
            log.error("Error creating a review for product ID [{}].", productId, e);
            throw new RuntimeException("An unexpected error occurred while creating the product review.");
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
                    existingReview.setRating(newValue);
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

    @Override
    public List<ProductReview> getReviewsByProductId(String productId){
        try {
            List<ProductReview> reviews = productReviewRepository.findByProductId(productId);
            log.info("Retrieved {} reviews for product ID [{}].", reviews.size(), productId);
            return reviews;
        } catch (Exception e) {
            log.error("Error retrieving reviews for product ID [{}].", productId, e);
            throw new RuntimeException("An unexpected error occurred while fetching reviews.");
        }
    }
}
