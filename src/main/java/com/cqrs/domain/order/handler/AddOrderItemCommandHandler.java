package com.cqrs.domain.order.handler;

import com.cqrs.domain.order.aggregate.Order;
import com.cqrs.domain.order.command.AddOrderItemCommand;
import com.cqrs.domain.order.repository.OrderWriteRepository;
import com.cqrs.infrastructure.command.CommandHandler;
import com.cqrs.infrastructure.event.EventBus;
import com.cqrs.infrastructure.exception.AggregateNotFoundException;
import com.cqrs.infrastructure.exception.CommandValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class AddOrderItemCommandHandler implements CommandHandler<AddOrderItemCommand, UUID> {

    private static final Logger log = LoggerFactory.getLogger(AddOrderItemCommandHandler.class);
    private final OrderWriteRepository repository;
    private final EventBus eventBus;

    public AddOrderItemCommandHandler(OrderWriteRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    @Override
    @Transactional
    public UUID handle(AddOrderItemCommand command) {
        if (command.getProductId() == null || command.getProductId().isBlank()) {
            throw new CommandValidationException("Product ID is required");
        }
        if (command.getProductName() == null || command.getProductName().isBlank()) {
            throw new CommandValidationException("Product name is required");
        }

        Order order = repository.findById(command.getOrderId())
                .orElseThrow(() -> new AggregateNotFoundException("Order", command.getOrderId()));

        order.addItem(command.getProductId(), command.getProductName(),
                command.getQuantity(), command.getUnitPrice(), command.getCorrelationId());

        repository.save(order);

        log.info("Item added to order: {} product: {}", command.getOrderId(), command.getProductId());

        eventBus.publishAll(order.getUncommittedEvents());
        order.clearEvents();

        return command.getOrderId();
    }

    @Override
    public Class<AddOrderItemCommand> getCommandType() {
        return AddOrderItemCommand.class;
    }
}
