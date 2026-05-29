package com.cqrs.domain.order.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequest(
        @NotBlank(message = "Customer ID is required") String customerId,
        @NotBlank(message = "Customer name is required") String customerName
) {}
