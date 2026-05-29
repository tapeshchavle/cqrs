package com.cqrs.infrastructure.query;

import com.cqrs.infrastructure.exception.QueryHandlerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class QueryBusImpl implements QueryBus {

    private static final Logger log = LoggerFactory.getLogger(QueryBusImpl.class);
    private final Map<Class<?>, QueryHandler<?, ?>> handlers = new HashMap<>();

    public QueryBusImpl(List<QueryHandler<?, ?>> handlerList) {
        handlerList.forEach(handler -> {
            handlers.put(handler.getQueryType(), handler);
            log.info("Registered query handler: {} -> {}", handler.getQueryType().getSimpleName(), handler.getClass().getSimpleName());
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R dispatch(Query<R> query) {
        log.debug("Dispatching query: {}", query.getClass().getSimpleName());

        QueryHandler<Query<R>, R> handler = (QueryHandler<Query<R>, R>) handlers.get(query.getClass());
        if (handler == null) {
            throw new QueryHandlerNotFoundException(query.getClass());
        }

        R result = handler.handle(query);
        log.debug("Query handled successfully: {}", query.getClass().getSimpleName());
        return result;
    }
}
