package com.cqrs.infrastructure.event;

import java.util.List;

public interface EventBus {
    void publish(DomainEvent event);
    void publishAll(List<DomainEvent> events);
}
