package com.shopio.review.controller;

import com.shopio.exception.ReviewNotBelongToProductException;
import com.shopio.exception.ReviewNotFoundException;
import com.shopio.review.entity.ProductReview;
import com.shopio.review.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ProductReviewController {
    private final ProductReviewService productReviewService;

    @GetMapping
    public ResponseEntity<List<ProductReview>> getAllReviews(){
        List<ProductReview> reviews = productReviewService.getAllProductReviews();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PostMapping("/create/{productId}")
    public ResponseEntity<ProductReview> createProductReview(
            @PathVariable String productId,
            @RequestBody ProductReview productReview){
        try {
            productReview.setProductId(productId);
            ProductReview createdReview = productReviewService.createProductReview(productId, productReview);

            if (createdReview != null) {
                return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductReview> getReviewById(@PathVariable String id){
        Optional<ProductReview> review = productReviewService.getProductReviewById(id);
        return review.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{productId}/{reviewId}/rating")
    public ResponseEntity<ProductReview> updateProductRating(
            @PathVariable String productId,
            @PathVariable String reviewId,
            @RequestBody Map<String, String> requestMap) throws ReviewNotFoundException, ReviewNotBelongToProductException, Exception{
        String newRating = requestMap.get("newRating");
        ProductReview updatedReview = productReviewService.updateProductReview(productId, reviewId, "rating", newRating);

        if (updatedReview == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }
    // TODO: 9/19/23 make it more generic to avoid code duplication

    @PatchMapping("/{productId}/{reviewId}/comment")
    public ResponseEntity<ProductReview> updateProductComment(
            @PathVariable String productId,
            @PathVariable String reviewId,
            @RequestBody Map<String, String> requestMap) throws ReviewNotFoundException, ReviewNotBelongToProductException, Exception{
        String newComment = requestMap.get("newComment");
        ProductReview updatedReview = productReviewService.updateProductReview(productId, reviewId, "comment", newComment);

        if (updatedReview == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id){
        productReviewService.deleteProductReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}