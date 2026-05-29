package com.cqrs.domain.order.event;

import com.cqrs.infrastructure.event.DomainEvent;
import java.math.BigDecimal;
import java.util.UUID;

public class OrderConfirmedEvent extends DomainEvent {
    private final BigDecimal totalAmount;

    public OrderConfirmedEvent(UUID aggregateId, String correlationId, long version, BigDecimal totalAmount) {
        super(aggregateId, "Order", correlationId, version);
        this.totalAmount = totalAmount;
    }

    @Override
    public String getEventType() { return "OrderConfirmed"; }
    public BigDecimal getTotalAmount() { return totalAmount; }
}
