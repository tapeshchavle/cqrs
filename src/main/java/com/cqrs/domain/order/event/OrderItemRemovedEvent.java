package com.cqrs.domain.order.event;

import com.cqrs.infrastructure.event.DomainEvent;
import java.util.UUID;

public class OrderItemRemovedEvent extends DomainEvent {
    private final UUID itemId;

    public OrderItemRemovedEvent(UUID aggregateId, String correlationId, long version, UUID itemId) {
        super(aggregateId, "Order", correlationId, version);
        this.itemId = itemId;
    }

    @Override
    public String getEventType() { return "OrderItemRemoved"; }
    public UUID getItemId() { return itemId; }
}
