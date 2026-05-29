package com.cqrs.domain.order.projection;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "order_summary_view", indexes = {
        @Index(name = "idx_order_summary_customer", columnList = "customerId"),
        @Index(name = "idx_order_summary_status", columnList = "status"),
        @Index(name = "idx_order_summary_created", columnList = "createdAt")
})
public class OrderSummaryProjection {

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

    @Column(nullable = false)
    private int itemCount;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected OrderSummaryProjection() {}

    public OrderSummaryProjection(UUID orderId, String customerId, String customerName, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.status = status;
        this.totalAmount = BigDecimal.ZERO;
        this.itemCount = 0;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public UUID getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getStatus() { return status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public int getItemCount() { return itemCount; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setStatus(String status) { this.status = status; this.updatedAt = Instant.now(); }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; this.updatedAt = Instant.now(); }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; this.updatedAt = Instant.now(); }
}
