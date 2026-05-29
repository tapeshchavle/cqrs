package com.cqrs.infrastructure.exception;

public class QueryHandlerNotFoundException extends RuntimeException {
    public QueryHandlerNotFoundException(Class<?> queryType) {
        super(String.format("No handler registered for query type: %s", queryType.getSimpleName()));
    }
}
