package com.cqrs.domain.order.handler;

import com.cqrs.domain.order.aggregate.Order;
import com.cqrs.domain.order.command.CancelOrderCommand;
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
public class CancelOrderCommandHandler implements CommandHandler<CancelOrderCommand, Void> {

    private static final Logger log = LoggerFactory.getLogger(CancelOrderCommandHandler.class);
    private final OrderWriteRepository repository;
    private final EventBus eventBus;

    public CancelOrderCommandHandler(OrderWriteRepository repository, EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    @Override
    @Transactional
    public Void handle(CancelOrderCommand command) {
        Order order = repository.findById(command.getOrderId())
                .orElseThrow(() -> new AggregateNotFoundException("Order", command.getOrderId()));

        order.cancel(command.getReason(), command.getCorrelationId());
        repository.save(order);

        log.info("Order cancelled: {} reason: {}", command.getOrderId(), command.getReason());

        eventBus.publishAll(order.getUncommittedEvents());
        order.clearEvents();

        return null;
    }

    @Override
    public Class<CancelOrderCommand> getCommandType() {
        return CancelOrderCommand.class;
    }
}
