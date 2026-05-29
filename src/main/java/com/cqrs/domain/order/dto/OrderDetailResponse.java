package com.cqrs.domain.order.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderDetailResponse(
        UUID orderId,
        String customerId,
        String customerName,
        String status,
        BigDecimal totalAmount,
        List<OrderItemResponse> items,
        Instant createdAt,
        Instant updatedAt
) {}
