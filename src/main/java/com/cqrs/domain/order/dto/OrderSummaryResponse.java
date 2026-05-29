package com.cqrs.domain.order.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record OrderSummaryResponse(
        UUID orderId,
        String customerId,
        String customerName,
        String status,
        BigDecimal totalAmount,
        int itemCount,
        Instant createdAt,
        Instant updatedAt
) {}
