package com.cqrs.domain.order.event;

import com.cqrs.infrastructure.event.DomainEvent;
import java.math.BigDecimal;
import java.util.UUID;

public class OrderItemAddedEvent extends DomainEvent {
    private final UUID itemId;
    private final String productId;
    private final String productName;
    private final int quantity;
    private final BigDecimal unitPrice;

    public OrderItemAddedEvent(UUID aggregateId, String correlationId, long version,
                               UUID itemId, String productId, String productName,
                               int quantity, BigDecimal unitPrice) {
        super(aggregateId, "Order", correlationId, version);
        this.itemId = itemId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    @Override
    public String getEventType() { return "OrderItemAdded"; }
    public UUID getItemId() { return itemId; }
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
}
