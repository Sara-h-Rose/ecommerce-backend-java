package com.sarahrose.ecommerce.order.dto;

import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        LocalDateTime orderDate,
        Long customerId,
        String customerName
) {
}