package com.shopio.review.repository;

import com.shopio.review.entity.ProductReview;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductReviewRepository extends MongoRepository<ProductReview, String> {
}
