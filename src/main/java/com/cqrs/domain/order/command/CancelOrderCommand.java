package com.cqrs.domain.order.command;

import com.cqrs.infrastructure.command.Command;
import java.util.UUID;

public class CancelOrderCommand extends Command {
    private final UUID orderId;
    private final String reason;

    public CancelOrderCommand(UUID orderId, String reason) {
        super();
        this.orderId = orderId;
        this.reason = reason;
    }

    public CancelOrderCommand(UUID orderId, String reason, String correlationId) {
        super(correlationId);
        this.orderId = orderId;
        this.reason = reason;
    }

    public UUID getOrderId() { return orderId; }
    public String getReason() { return reason; }
}
