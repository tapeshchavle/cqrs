package com.cqrs.domain.order.event;

import com.cqrs.infrastructure.event.DomainEvent;
import java.util.UUID;

public class OrderCreatedEvent extends DomainEvent {
    private final String customerId;
    private final String customerName;

    public OrderCreatedEvent(UUID aggregateId, String correlationId, long version,
                             String customerId, String customerName) {
        super(aggregateId, "Order", correlationId, version);
        this.customerId = customerId;
        this.customerName = customerName;
    }

    @Override
    public String getEventType() { return "OrderCreated"; }
    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
}
