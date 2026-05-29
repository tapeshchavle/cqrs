package com.cqrs.domain.order.aggregate;

public enum OrderStatus {
    CREATED,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
