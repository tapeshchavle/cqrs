package com.cqrs.domain.order.command;

import com.cqrs.infrastructure.command.Command;
import java.util.UUID;

public class RemoveOrderItemCommand extends Command {
    private final UUID orderId;
    private final UUID itemId;

    public RemoveOrderItemCommand(UUID orderId, UUID itemId) {
        super();
        this.orderId = orderId;
        this.itemId = itemId;
    }

    public RemoveOrderItemCommand(UUID orderId, UUID itemId, String correlationId) {
        super(correlationId);
        this.orderId = orderId;
        this.itemId = itemId;
    }

    public UUID getOrderId() { return orderId; }
    public UUID getItemId() { return itemId; }
}
