package com.sarahrose.ecommerce.product.dto;

import com.sarahrose.ecommerce.product.model.Category;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer quantity,
        Category category
) {
}