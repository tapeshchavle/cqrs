package com.cqrs.domain.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record AddOrderItemRequest(
        @NotBlank(message = "Product ID is required") String productId,
        @NotBlank(message = "Product name is required") String productName,
        @Min(value = 1, message = "Quantity must be at least 1") int quantity,
        @NotNull(message = "Unit price is required") BigDecimal unitPrice
) {}
