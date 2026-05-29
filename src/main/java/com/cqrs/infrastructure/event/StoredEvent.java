package com.cqrs.infrastructure.event;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "stored_events", indexes = {
        @Index(name = "idx_stored_event_aggregate_id", columnList = "aggregateId"),
        @Index(name = "idx_stored_event_aggregate_type", columnList = "aggregateType"),
        @Index(name = "idx_stored_event_correlation_id", columnList = "correlationId"),
        @Index(name = "idx_stored_event_occurred_at", columnList = "occurredAt")
})
public class StoredEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID eventId;

    @Column(nullable = false)
    private UUID aggregateId;

    @Column(nullable = false)
    private String aggregateType;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private Instant occurredAt;

    @Column(nullable = false)
    private String correlationId;

    @Column(nullable = false)
    private long version;

    protected StoredEvent() {}

    public StoredEvent(DomainEvent event, String serializedPayload) {
        this.eventId = event.getEventId();
        this.aggregateId = event.getAggregateId();
        this.aggregateType = event.getAggregateType();
        this.eventType = event.getEventType();
        this.payload = serializedPayload;
        this.occurredAt = event.getOccurredAt();
        this.correlationId = event.getCorrelationId();
        this.version = event.getVersion();
    }

    public Long getId() { return id; }
    public UUID getEventId() { return eventId; }
    public UUID getAggregateId() { return aggregateId; }
    public String getAggregateType() { return aggregateType; }
    public String getEventType() { return eventType; }
    public String getPayload() { return payload; }
    public Instant getOccurredAt() { return occurredAt; }
    public String getCorrelationId() { return correlationId; }
    public long getVersion() { return version; }
}
