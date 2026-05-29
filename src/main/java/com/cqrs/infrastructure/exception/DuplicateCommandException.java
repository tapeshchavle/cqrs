package com.cqrs.infrastructure.exception;

import java.util.UUID;

public class DuplicateCommandException extends RuntimeException {
    private final UUID commandId;

    public DuplicateCommandException(UUID commandId) {
        super(String.format("Command with id '%s' has already been processed", commandId));
        this.commandId = commandId;
    }

    public UUID getCommandId() { return commandId; }
}
