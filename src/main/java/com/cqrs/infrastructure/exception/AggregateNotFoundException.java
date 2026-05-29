package com.cqrs.infrastructure.exception;

import java.util.UUID;

public class AggregateNotFoundException extends RuntimeException {
    private final String aggregateType;
    private final UUID aggregateId;

    public AggregateNotFoundException(String aggregateType, UUID aggregateId) {
        super(String.format("%s with id '%s' not found", aggregateType, aggregateId));
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
    }

    public String getAggregateType() { return aggregateType; }
    public UUID getAggregateId() { return aggregateId; }
}
