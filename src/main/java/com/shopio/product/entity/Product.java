package com.shopio.product.entity;

import com.shopio.review.entity.ProductReview;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Size(min = 2, max = 255, message = "Product name must be between 2 and 255 characters")
    private String name;

    @NotEmpty
    @Size(min = 10, max = 255, message = "Product description must be between 10 and 255 characters")
    private String description;

    @Min(2)
    @Max(9999)
    private double price;

    @Min(0)
    @Max(10000)
    private int amount;

    @Nullable
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductReview> reviews = new LinkedList<>();

    @Formula("(SELECT COUNT(*) FROM product_review pr WHERE pr.product_id = id)")
    private int countReviews;

    @Enumerated(value = EnumType.STRING)
    private Category category;

}

