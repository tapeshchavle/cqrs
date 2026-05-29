package com.cqrs.domain.order.query;

import com.cqrs.domain.order.dto.OrderSummaryResponse;
import com.cqrs.infrastructure.query.Query;
import org.springframework.data.domain.Page;

public class GetAllOrdersQuery implements Query<Page<OrderSummaryResponse>> {
    private final int page;
    private final int size;

    public GetAllOrdersQuery(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getPage() { return page; }
    public int getSize() { return size; }
}
