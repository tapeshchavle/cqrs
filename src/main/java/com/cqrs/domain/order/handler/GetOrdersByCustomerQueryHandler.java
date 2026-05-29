package com.cqrs.domain.order.handler;

import com.cqrs.domain.order.dto.OrderSummaryResponse;
import com.cqrs.domain.order.query.GetOrdersByCustomerQuery;
import com.cqrs.domain.order.repository.OrderSummaryReadRepository;
import com.cqrs.infrastructure.query.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GetOrdersByCustomerQueryHandler implements QueryHandler<GetOrdersByCustomerQuery, List<OrderSummaryResponse>> {

    private final OrderSummaryReadRepository repository;

    public GetOrdersByCustomerQueryHandler(OrderSummaryReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<OrderSummaryResponse> handle(GetOrdersByCustomerQuery query) {
        return repository.findByCustomerId(query.getCustomerId()).stream()
                .map(p -> new OrderSummaryResponse(
                        p.getOrderId(), p.getCustomerId(), p.getCustomerName(),
                        p.getStatus(), p.getTotalAmount(), p.getItemCount(),
                        p.getCreatedAt(), p.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Class<GetOrdersByCustomerQuery> getQueryType() {
        return GetOrdersByCustomerQuery.class;
    }
}
