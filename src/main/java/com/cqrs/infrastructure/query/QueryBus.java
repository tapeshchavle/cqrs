package com.cqrs.infrastructure.query;

public interface QueryBus {
    <R> R dispatch(Query<R> query);
}
