package com.cqrs.infrastructure.command;

import com.cqrs.infrastructure.exception.CommandHandlerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandBusImpl implements CommandBus {

    private static final Logger log = LoggerFactory.getLogger(CommandBusImpl.class);
    private final Map<Class<?>, CommandHandler<?, ?>> handlers = new HashMap<>();

    public CommandBusImpl(List<CommandHandler<?, ?>> handlerList) {
        handlerList.forEach(handler -> {
            handlers.put(handler.getCommandType(), handler);
            log.info("Registered command handler: {} -> {}", handler.getCommandType().getSimpleName(), handler.getClass().getSimpleName());
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R dispatch(Command command) {
        MDC.put("correlationId", command.getCorrelationId());
        try {
            log.debug("Dispatching command: {} [id={}]", command.getClass().getSimpleName(), command.getCommandId());

            CommandHandler<Command, R> handler = (CommandHandler<Command, R>) handlers.get(command.getClass());
            if (handler == null) {
                throw new CommandHandlerNotFoundException(command.getClass());
            }

            R result = handler.handle(command);
            log.debug("Command handled successfully: {} [id={}]", command.getClass().getSimpleName(), command.getCommandId());
            return result;
        } finally {
            MDC.remove("correlationId");
        }
    }
}
