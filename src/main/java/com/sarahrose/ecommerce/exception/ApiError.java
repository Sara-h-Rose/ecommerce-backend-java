package com.sarahrose.ecommerce.exception;

public record ApiError(
        int status,
        String message
) {
}
