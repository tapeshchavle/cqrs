package com.cqrs.domain.order.query;

import com.cqrs.domain.order.dto.OrderSummaryResponse;
import com.cqrs.infrastructure.query.Query;
import java.util.List;

public class GetOrdersByStatusQuery implements Query<List<OrderSummaryResponse>> {
    private final String status;

    public GetOrdersByStatusQuery(String status) {
        this.status = status;
    }

    public String getStatus() { return status; }
}
