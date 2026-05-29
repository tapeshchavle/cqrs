package com.cqrs.infrastructure.command;

import java.time.Instant;
import java.util.UUID;

public abstract class Command {
    private final UUID commandId;
    private final String correlationId;
    private final Instant timestamp;

    protected Command() {
        this.commandId = UUID.randomUUID();
        this.correlationId = UUID.randomUUID().toString();
        this.timestamp = Instant.now();
    }

    protected Command(String correlationId) {
        this.commandId = UUID.randomUUID();
        this.correlationId = correlationId != null ? correlationId : UUID.randomUUID().toString();
        this.timestamp = Instant.now();
    }

    public UUID getCommandId() { return commandId; }
    public String getCorrelationId() { return correlationId; }
    public Instant getTimestamp() { return timestamp; }
}
