package com.sarahrose.ecommerce.dto;

import com.sarahrose.ecommerce.enums.Category;

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
