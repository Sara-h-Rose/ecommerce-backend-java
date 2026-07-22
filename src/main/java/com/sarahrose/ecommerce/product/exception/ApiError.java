package com.sarahrose.ecommerce.product.exception;

public record ApiError(
        int status,
        String message
) {
}