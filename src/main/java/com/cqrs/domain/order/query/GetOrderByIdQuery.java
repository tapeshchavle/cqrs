package com.cqrs.domain.order.query;

import com.cqrs.domain.order.dto.OrderDetailResponse;
import com.cqrs.infrastructure.query.Query;
import java.util.UUID;

public class GetOrderByIdQuery implements Query<OrderDetailResponse> {
    private final UUID orderId;

    public GetOrderByIdQuery(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getOrderId() { return orderId; }
}
