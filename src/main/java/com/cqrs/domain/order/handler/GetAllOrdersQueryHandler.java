package com.cqrs.domain.order.handler;

import com.cqrs.domain.order.dto.OrderSummaryResponse;
import com.cqrs.domain.order.query.GetAllOrdersQuery;
import com.cqrs.domain.order.repository.OrderSummaryReadRepository;
import com.cqrs.infrastructure.query.QueryHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class GetAllOrdersQueryHandler implements QueryHandler<GetAllOrdersQuery, Page<OrderSummaryResponse>> {

    private final OrderSummaryReadRepository repository;

    public GetAllOrdersQueryHandler(OrderSummaryReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<OrderSummaryResponse> handle(GetAllOrdersQuery query) {
        return repository.findAll(PageRequest.of(query.getPage(), query.getSize(), Sort.by(Sort.Direction.DESC, "createdAt")))
                .map(p -> new OrderSummaryResponse(
                        p.getOrderId(), p.getCustomerId(), p.getCustomerName(),
                        p.getStatus(), p.getTotalAmount(), p.getItemCount(),
                        p.getCreatedAt(), p.getUpdatedAt()
                ));
    }

    @Override
    public Class<GetAllOrdersQuery> getQueryType() {
        return GetAllOrdersQuery.class;
    }
}
