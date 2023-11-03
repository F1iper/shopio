package com.shopio.product.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Enumerated(value = EnumType.STRING)
    private Category category;

}

