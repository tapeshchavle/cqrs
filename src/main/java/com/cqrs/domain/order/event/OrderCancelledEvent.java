package com.cqrs.domain.order.event;

import com.cqrs.infrastructure.event.DomainEvent;
import java.util.UUID;

public class OrderCancelledEvent extends DomainEvent {
    private final String reason;

    public OrderCancelledEvent(UUID aggregateId, String correlationId, long version, String reason) {
        super(aggregateId, "Order", correlationId, version);
        this.reason = reason;
    }

    @Override
    public String getEventType() { return "OrderCancelled"; }
    public String getReason() { return reason; }
}
