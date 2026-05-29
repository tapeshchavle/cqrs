package com.cqrs.domain.order.command;

import com.cqrs.infrastructure.command.Command;
import java.math.BigDecimal;
import java.util.UUID;

public class AddOrderItemCommand extends Command {
    private final UUID orderId;
    private final String productId;
    private final String productName;
    private final int quantity;
    private final BigDecimal unitPrice;

    public AddOrderItemCommand(UUID orderId, String productId, String productName,
                               int quantity, BigDecimal unitPrice) {
        super();
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public AddOrderItemCommand(UUID orderId, String productId, String productName,
                               int quantity, BigDecimal unitPrice, String correlationId) {
        super(correlationId);
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public UUID getOrderId() { return orderId; }
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
}
