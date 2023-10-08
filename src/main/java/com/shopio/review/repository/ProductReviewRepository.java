package com.shopio.review.repository;

import com.shopio.review.entity.ProductReview;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductReviewRepository extends MongoRepository<ProductReview, String> {
    List<ProductReview> findByProductId(String id);
}
