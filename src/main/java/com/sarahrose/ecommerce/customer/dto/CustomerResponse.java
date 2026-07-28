package com.sarahrose.ecommerce.customer.dto;

public record CustomerResponse(
        Long id,
        String name,
        String email
) {
}