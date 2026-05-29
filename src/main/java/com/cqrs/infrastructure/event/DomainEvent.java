package com.cqrs.infrastructure.event;

import java.time.Instant;
import java.util.UUID;

public abstract class DomainEvent {
    private final UUID eventId;
    private final UUID aggregateId;
    private final String aggregateType;
    private final Instant occurredAt;
    private final String correlationId;
    private final long version;

    protected DomainEvent(UUID aggregateId, String aggregateType, String correlationId, long version) {
        this.eventId = UUID.randomUUID();
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.occurredAt = Instant.now();
        this.correlationId = correlationId;
        this.version = version;
    }

    public UUID getEventId() { return eventId; }
    public UUID getAggregateId() { return aggregateId; }
    public String getAggregateType() { return aggregateType; }
    public Instant getOccurredAt() { return occurredAt; }
    public String getCorrelationId() { return correlationId; }
    public long getVersion() { return version; }

    public abstract String getEventType();
}
