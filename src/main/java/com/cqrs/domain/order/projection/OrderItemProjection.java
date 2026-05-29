package com.cqrs.domain.order.projection;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_item_view")
public class OrderItemProjection {

    @Id
    private UUID itemId;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    protected OrderItemProjection() {}

    public OrderItemProjection(UUID itemId, String productId, String productName,
                               int quantity, BigDecimal unitPrice) {
        this.itemId = itemId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public UUID getItemId() { return itemId; }
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
}
