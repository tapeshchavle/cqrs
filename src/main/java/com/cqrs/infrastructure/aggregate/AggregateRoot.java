package com.cqrs.infrastructure.aggregate;

import com.cqrs.infrastructure.event.DomainEvent;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@MappedSuperclass
public abstract class AggregateRoot {

    @Id
    private UUID id;

    @Version
    private Long version;

    @Transient
    private final List<DomainEvent> uncommittedEvents = new ArrayList<>();

    protected AggregateRoot() {
        this.id = UUID.randomUUID();
    }

    protected AggregateRoot(UUID id) {
        this.id = id;
    }

    public UUID getId() { return id; }
    public Long getVersion() { return version; }

    protected void registerEvent(DomainEvent event) {
        uncommittedEvents.add(event);
    }

    public List<DomainEvent> getUncommittedEvents() {
        return Collections.unmodifiableList(uncommittedEvents);
    }

    public void clearEvents() {
        uncommittedEvents.clear();
    }
}
