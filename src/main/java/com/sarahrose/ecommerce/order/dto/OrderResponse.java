package com.sarahrose.ecommerce.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        LocalDateTime orderDate,
        Long customerId,
        String customerName,
        List<OrderItemResponse> items,
        BigDecimal total
) {
}