package com.cqrs.domain.order.handler;

import com.cqrs.domain.order.aggregate.Order;
import com.cqrs.domain.order.command.CreateOrderCommand;
import com.cqrs.domain.order.repository.OrderWriteRepository;
import com.cqrs.infrastructure.command.CommandHandler;
import com.cqrs.infrastructure.event.EventBus;
import com.cqrs.infrastructure.exception.CommandValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class CreateOrderCommandHandler implements CommandHandler<CreateOrderCommand, UUID> {

    private static final Logger log = LoggerFactory.getLogger(CreateOrderCommandHandler.class);
    private final OrderWriteRepository repository;
    private final EventBus eventBus;

    public CreateOrderCommandHandler(OrderWriteRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    @Override
    @Transactional
    public UUID handle(CreateOrderCommand command) {
        if (command.getCustomerId() == null || command.getCustomerId().isBlank()) {
            throw new CommandValidationException("Customer ID is required");
        }
        if (command.getCustomerName() == null || command.getCustomerName().isBlank()) {
            throw new CommandValidationException("Customer name is required");
        }

        Order order = Order.create(command.getCustomerId(), command.getCustomerName(), command.getCorrelationId());
        repository.save(order);

        log.info("Order created: {} for customer: {}", order.getId(), command.getCustomerId());

        eventBus.publishAll(order.getUncommittedEvents());
        order.clearEvents();

        return order.getId();
    }

    @Override
    public Class<CreateOrderCommand> getCommandType() {
        return CreateOrderCommand.class;
    }
}
