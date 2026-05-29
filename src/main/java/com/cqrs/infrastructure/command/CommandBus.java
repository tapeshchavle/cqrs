package com.cqrs.infrastructure.command;

public interface CommandBus {
    <R> R dispatch(Command command);
}
