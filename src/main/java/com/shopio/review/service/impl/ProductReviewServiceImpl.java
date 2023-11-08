package com.shopio.review.service.impl;

import com.shopio.exception.ResourceNotFoundException;
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
public class ProductReviewServiceImpl implements ProductReviewService {
    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;

    @Override
    public ProductReview createProductReview(Long productId, ProductReview productReview) {
        Product existingProduct = productRepository.findById(productId)
                .orElse(null);

        if (existingProduct == null) {
            log.error("Product with ID: {} does not exist.", productId);
            // TODO: 11/9/2023 handle exception instead throw null
            return null;
        }

        productReview.setProduct(existingProduct);
        ProductReview createdReview = productReviewRepository.save(productReview);

        log.info("Created a new review with ID [{}] for product [{}].", createdReview.getId(), productId);

        return createdReview;
    }

    @Override
    public ProductReview updateProductReview(Long productId, Long reviewId, String updateField, String newValue) {
        try {
            ProductReview existingReview = productReviewRepository.findById(reviewId)
                    .orElseThrow(() -> new ReviewNotFoundException("Review with ID: [" + reviewId + "] not found."));

            if (!productId.equals(existingReview.getProduct().getId())) {
                throw new ReviewNotBelongToProductException("Review with ID [" + reviewId + "] does not belong to a specific product with ID [" + productId + "].");
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
        } catch (ReviewNotBelongToProductException | ReviewNotFoundException | IllegalArgumentException exception) {
            log.error("Error updating product review.", exception);
            return null;
        }
    }

    @Override
    public List<ProductReview> getAllProductReviews(){
        return productReviewRepository.findAll();
    }

    @Override
    public Optional<ProductReview> getProductReviewById(Long id){
        return productReviewRepository.findById(id);
    }

    @Override
    public void deleteProductReview(Long id){
        if (productReviewRepository.existsById(id)) {
            productReviewRepository.deleteById(id);
        }
    }

    @Override
    public List<ProductReview> getReviewsByProductId(Long productId){
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
