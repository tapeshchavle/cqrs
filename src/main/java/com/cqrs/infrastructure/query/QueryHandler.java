package com.cqrs.infrastructure.query;

public interface QueryHandler<Q extends Query<R>, R> {
    R handle(Q query);
    Class<Q> getQueryType();
}
