package com.cqrs.domain.order.query;

import com.cqrs.domain.order.dto.OrderSummaryResponse;
import com.cqrs.infrastructure.query.Query;
import java.util.List;

public class GetOrdersByCustomerQuery implements Query<List<OrderSummaryResponse>> {
    private final String customerId;

    public GetOrdersByCustomerQuery(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerId() { return customerId; }
}
