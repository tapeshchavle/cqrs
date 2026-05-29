package com.cqrs.domain.order.handler;

import com.cqrs.domain.order.dto.OrderDetailResponse;
import com.cqrs.domain.order.dto.OrderItemResponse;
import com.cqrs.domain.order.projection.OrderDetailProjection;
import com.cqrs.domain.order.query.GetOrderByIdQuery;
import com.cqrs.domain.order.repository.OrderDetailReadRepository;
import com.cqrs.infrastructure.exception.AggregateNotFoundException;
import com.cqrs.infrastructure.query.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GetOrderByIdQueryHandler implements QueryHandler<GetOrderByIdQuery, OrderDetailResponse> {

    private final OrderDetailReadRepository repository;

    public GetOrderByIdQueryHandler(OrderDetailReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public OrderDetailResponse handle(GetOrderByIdQuery query) {
        OrderDetailProjection detail = repository.findById(query.getOrderId())
                .orElseThrow(() -> new AggregateNotFoundException("Order", query.getOrderId()));

        return new OrderDetailResponse(
                detail.getOrderId(),
                detail.getCustomerId(),
                detail.getCustomerName(),
                detail.getStatus(),
                detail.getTotalAmount(),
                detail.getItems().stream()
                        .map(item -> new OrderItemResponse(
                                item.getItemId(), item.getProductId(), item.getProductName(),
                                item.getQuantity(), item.getUnitPrice(),
                                item.getUnitPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity()))
                        ))
                        .collect(Collectors.toList()),
                detail.getCreatedAt(),
                detail.getUpdatedAt()
        );
    }

    @Override
    public Class<GetOrderByIdQuery> getQueryType() {
        return GetOrderByIdQuery.class;
    }
}
