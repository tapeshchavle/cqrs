package com.cqrs.infrastructure.command;

public interface CommandHandler<C extends Command, R> {
    R handle(C command);
    Class<C> getCommandType();
}
