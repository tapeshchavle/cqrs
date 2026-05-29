package com.cqrs.infrastructure.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EventStore extends JpaRepository<StoredEvent, Long> {
    List<StoredEvent> findByAggregateIdOrderByVersionAsc(UUID aggregateId);
    List<StoredEvent> findByAggregateTypeOrderByOccurredAtAsc(String aggregateType);
    List<StoredEvent> findByCorrelationId(String correlationId);
}
