package com.cqrs.domain.order.handler;

import com.cqrs.domain.order.dto.OrderSummaryResponse;
import com.cqrs.domain.order.query.GetOrdersByStatusQuery;
import com.cqrs.domain.order.repository.OrderSummaryReadRepository;
import com.cqrs.infrastructure.query.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetOrdersByStatusQueryHandler implements QueryHandler<GetOrdersByStatusQuery, List<OrderSummaryResponse>> {

    private final OrderSummaryReadRepository repository;

    public GetOrdersByStatusQueryHandler(OrderSummaryReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<OrderSummaryResponse> handle(GetOrdersByStatusQuery query) {
        return repository.findByStatus(query.getStatus()).stream()
                .map(p -> new OrderSummaryResponse(
                        p.getOrderId(), p.getCustomerId(), p.getCustomerName(),
                        p.getStatus(), p.getTotalAmount(), p.getItemCount(),
                        p.getCreatedAt(), p.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Class<GetOrdersByStatusQuery> getQueryType() {
        return GetOrdersByStatusQuery.class;
    }
}
