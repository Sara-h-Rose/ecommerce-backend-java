package com.sarahrose.ecommerce.dto;

public record CustomerResponse(
        Long id,
        String name,
        String email,
        boolean active
) {
}
