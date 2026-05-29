package com.cqrs.domain.order.handler;

import com.cqrs.domain.order.aggregate.Order;
import com.cqrs.domain.order.command.RemoveOrderItemCommand;
import com.cqrs.domain.order.repository.OrderWriteRepository;
import com.cqrs.infrastructure.command.CommandHandler;
import com.cqrs.infrastructure.event.EventBus;
import com.cqrs.infrastructure.exception.AggregateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class RemoveOrderItemCommandHandler implements CommandHandler<RemoveOrderItemCommand, Void> {

    private static final Logger log = LoggerFactory.getLogger(RemoveOrderItemCommandHandler.class);
    private final OrderWriteRepository repository;
    private final EventBus eventBus;

    public RemoveOrderItemCommandHandler(OrderWriteRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    @Override
    @Transactional
    public Void handle(RemoveOrderItemCommand command) {
        Order order = repository.findById(command.getOrderId())
                .orElseThrow(() -> new AggregateNotFoundException("Order", command.getOrderId()));

        order.removeItem(command.getItemId(), command.getCorrelationId());
        repository.save(order);

        log.info("Item removed from order: {} item: {}", command.getOrderId(), command.getItemId());

        eventBus.publishAll(order.getUncommittedEvents());
        order.clearEvents();

        return null;
    }

    @Override
    public Class<RemoveOrderItemCommand> getCommandType() {
        return RemoveOrderItemCommand.class;
    }
}
