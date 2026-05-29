package com.cqrs.domain.order.projection;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order_detail_view")
public class OrderDetailProjection {

    @Id
    private UUID orderId;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_detail_id")
    private List<OrderItemProjection> items = new ArrayList<>();

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected OrderDetailProjection() {}

    public OrderDetailProjection(UUID orderId, String customerId, String customerName, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.status = status;
        this.totalAmount = BigDecimal.ZERO;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public UUID getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public List<OrderItemProjection> getItems() { return items; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setStatus(String status) { this.status = status; this.updatedAt = Instant.now(); }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; this.updatedAt = Instant.now(); }

    public void addItem(OrderItemProjection item) {
        items.add(item);
        recalculateTotal();
        this.updatedAt = Instant.now();
    }

    public void removeItem(UUID itemId) {
        items.removeIf(item -> item.getItemId().equals(itemId));
        recalculateTotal();
        this.updatedAt = Instant.now();
    }

    private void recalculateTotal() {
        this.totalAmount = items.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
