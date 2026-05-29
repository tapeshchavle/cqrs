package com.cqrs.domain.order.aggregate;

import com.cqrs.domain.order.event.*;
import com.cqrs.infrastructure.aggregate.AggregateRoot;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order extends AggregateRoot {

    private static final String AGGREGATE_TYPE = "Order";

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items = new ArrayList<>();

    @Column(nullable = false)
    private Instant createdAt;

    private Instant updatedAt;

    protected Order() { super(); }

    public static Order create(String customerId, String customerName, String correlationId) {
        Order order = new Order();
        order.customerId = customerId;
        order.customerName = customerName;
        order.status = OrderStatus.CREATED;
        order.createdAt = Instant.now();
        order.updatedAt = order.createdAt;

        order.registerEvent(new OrderCreatedEvent(
                order.getId(), correlationId, 1L, customerId, customerName
        ));

        return order;
    }

    public void addItem(String productId, String productName, int quantity, BigDecimal unitPrice, String correlationId) {
        validateModifiable();
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        if (unitPrice.compareTo(BigDecimal.ZERO) <= 0) throw new IllegalArgumentException("Unit price must be positive");

        OrderItem item = new OrderItem(productId, productName, quantity, unitPrice);
        items.add(item);
        updatedAt = Instant.now();

        registerEvent(new OrderItemAddedEvent(
                getId(), correlationId, getVersion() != null ? getVersion() + 1 : 1L,
                item.getId(), productId, productName, quantity, unitPrice
        ));
    }

    public void removeItem(UUID itemId, String correlationId) {
        validateModifiable();
        OrderItem item = items.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item not found: " + itemId));

        items.remove(item);
        updatedAt = Instant.now();

        registerEvent(new OrderItemRemovedEvent(
                getId(), correlationId, getVersion() != null ? getVersion() + 1 : 1L, itemId
        ));
    }

    public void confirm(String correlationId) {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Cannot confirm order in status: " + status);
        }
        if (items.isEmpty()) {
            throw new IllegalStateException("Cannot confirm order with no items");
        }
        status = OrderStatus.CONFIRMED;
        updatedAt = Instant.now();

        registerEvent(new OrderConfirmedEvent(
                getId(), correlationId, getVersion() != null ? getVersion() + 1 : 1L, getTotalAmount()
        ));
    }

    public void cancel(String reason, String correlationId) {
        if (status == OrderStatus.SHIPPED || status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot cancel order in status: " + status);
        }
        if (status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled");
        }
        status = OrderStatus.CANCELLED;
        updatedAt = Instant.now();

        registerEvent(new OrderCancelledEvent(
                getId(), correlationId, getVersion() != null ? getVersion() + 1 : 1L, reason
        ));
    }

    public BigDecimal getTotalAmount() {
        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateModifiable() {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Cannot modify order in status: " + status);
        }
    }

    // Getters
    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public OrderStatus getStatus() { return status; }
    public List<OrderItem> getItems() { return List.copyOf(items); }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
