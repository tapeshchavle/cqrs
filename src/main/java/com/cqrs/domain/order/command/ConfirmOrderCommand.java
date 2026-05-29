package com.cqrs.domain.order.command;

import com.cqrs.infrastructure.command.Command;
import java.util.UUID;

public class ConfirmOrderCommand extends Command {
    private final UUID orderId;

    public ConfirmOrderCommand(UUID orderId) {
        super();
        this.orderId = orderId;
    }

    public ConfirmOrderCommand(UUID orderId, String correlationId) {
        super(correlationId);
        this.orderId = orderId;
    }

    public UUID getOrderId() { return orderId; }
}
