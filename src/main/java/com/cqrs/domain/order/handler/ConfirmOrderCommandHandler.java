package com.cqrs.domain.order.handler;

import com.cqrs.domain.order.aggregate.Order;
import com.cqrs.domain.order.command.ConfirmOrderCommand;
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
public class ConfirmOrderCommandHandler implements CommandHandler<ConfirmOrderCommand, Void> {

    private static final Logger log = LoggerFactory.getLogger(ConfirmOrderCommandHandler.class);
    private final OrderWriteRepository repository;
    private final EventBus eventBus;

    public ConfirmOrderCommandHandler(OrderWriteRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    @Override
    @Transactional
    public Void handle(ConfirmOrderCommand command) {
        Order order = repository.findById(command.getOrderId())
                .orElseThrow(() -> new AggregateNotFoundException("Order", command.getOrderId()));

        order.confirm(command.getCorrelationId());
        repository.save(order);

        log.info("Order confirmed: {}", command.getOrderId());

        eventBus.publishAll(order.getUncommittedEvents());
        order.clearEvents();

        return null;
    }

    @Override
    public Class<ConfirmOrderCommand> getCommandType() {
        return ConfirmOrderCommand.class;
    }
}
