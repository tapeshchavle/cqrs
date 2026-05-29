package com.cqrs.domain.order.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemResponse(
        UUID itemId,
        String productId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal totalPrice
) {}
