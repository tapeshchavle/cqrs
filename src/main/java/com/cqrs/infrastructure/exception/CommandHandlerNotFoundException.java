package com.cqrs.infrastructure.exception;

public class CommandHandlerNotFoundException extends RuntimeException {
    public CommandHandlerNotFoundException(Class<?> commandType) {
        super(String.format("No handler registered for command type: %s", commandType.getSimpleName()));
    }
}
