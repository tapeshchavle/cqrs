package com.cqrs.infrastructure.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringEventBus implements EventBus {

    private static final Logger log = LoggerFactory.getLogger(SpringEventBus.class);
    private final ApplicationEventPublisher publisher;
    private final EventStore eventStore;

    public SpringEventBus(ApplicationEventPublisher publisher, EventStore eventStore) {
        this.publisher = publisher;
        this.eventStore = eventStore;
    }

    @Override
    public void publish(DomainEvent event) {
        log.debug("Publishing event: {} [aggregateId={}, correlationId={}]",
                event.getEventType(), event.getAggregateId(), event.getCorrelationId());
        eventStore.save(new StoredEvent(event, event.toString()));
        publisher.publishEvent(event);
    }

    @Override
    public void publishAll(List<DomainEvent> events) {
        events.forEach(this::publish);
    }
}
