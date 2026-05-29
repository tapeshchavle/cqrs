package com.cqrs.domain.order.command;

import com.cqrs.infrastructure.command.Command;

public class CreateOrderCommand extends Command {
    private final String customerId;
    private final String customerName;

    public CreateOrderCommand(String customerId, String customerName) {
        super();
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public CreateOrderCommand(String customerId, String customerName, String correlationId) {
        super(correlationId);
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
}
