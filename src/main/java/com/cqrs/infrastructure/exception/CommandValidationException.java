package com.cqrs.infrastructure.exception;

import java.util.List;
import java.util.Map;

public class CommandValidationException extends RuntimeException {
    private final Map<String, List<String>> errors;

    public CommandValidationException(String message) {
        super(message);
        this.errors = Map.of("general", List.of(message));
    }

    public CommandValidationException(Map<String, List<String>> errors) {
        super("Command validation failed: " + errors);
        this.errors = errors;
    }

    public Map<String, List<String>> getErrors() { return errors; }
}
