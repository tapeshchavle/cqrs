package com.cqrs.api;

import com.cqrs.domain.order.command.*;
import com.cqrs.domain.order.dto.AddOrderItemRequest;
import com.cqrs.domain.order.dto.CancelOrderRequest;
import com.cqrs.domain.order.dto.CreateOrderRequest;
import com.cqrs.infrastructure.command.CommandBus;
import com.cqrs.infrastructure.web.CorrelationIdFilter;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

// Maintance all the post , delete , patch Apis
@RestController
@RequestMapping("/api/orders")
public class OrderCommandController {

    private static final Logger log = LoggerFactory.getLogger(OrderCommandController.class);
    private final CommandBus commandBus;

    public OrderCommandController(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        String correlationId = MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
        log.info("Creating order for customer: {}", request.customerId());

        CreateOrderCommand command = new CreateOrderCommand(
                request.customerId(), request.customerName(), correlationId
        );

        UUID orderId = commandBus.dispatch(command);

        return ResponseEntity
                .created(URI.create("/api/orders/" + orderId))
                .body(Map.of(
                        "orderId", orderId,
                        "message", "Order created successfully",
                        "correlationId", correlationId != null ? correlationId : ""
                ));
    }

    @PostMapping("/{orderId}/items")
    public ResponseEntity<Map<String, Object>> addOrderItem(
            @PathVariable UUID orderId,
            @Valid @RequestBody AddOrderItemRequest request) {
        String correlationId = MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
        log.info("Adding item to order: {} product: {}", orderId, request.productId());

        AddOrderItemCommand command = new AddOrderItemCommand(
                orderId, request.productId(), request.productName(),
                request.quantity(), request.unitPrice(), correlationId
        );

        commandBus.dispatch(command);

        return ResponseEntity.ok(Map.of(
                "orderId", orderId,
                "message", "Item added successfully",
                "correlationId", correlationId != null ? correlationId : ""
        ));
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<Map<String, Object>> removeOrderItem(
            @PathVariable UUID orderId,
            @PathVariable UUID itemId) {
        String correlationId = MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
        log.info("Removing item from order: {} item: {}", orderId, itemId);

        RemoveOrderItemCommand command = new RemoveOrderItemCommand(orderId, itemId, correlationId);
        commandBus.dispatch(command);

        return ResponseEntity.ok(Map.of(
                "orderId", orderId,
                "message", "Item removed successfully",
                "correlationId", correlationId != null ? correlationId : ""
        ));
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<Map<String, Object>> confirmOrder(@PathVariable UUID orderId) {
        String correlationId = MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
        log.info("Confirming order: {}", orderId);

        ConfirmOrderCommand command = new ConfirmOrderCommand(orderId, correlationId);
        commandBus.dispatch(command);

        return ResponseEntity.ok(Map.of(
                "orderId", orderId,
                "message", "Order confirmed successfully",
                "correlationId", correlationId != null ? correlationId : ""
        ));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrder(
            @PathVariable UUID orderId,
            @Valid @RequestBody CancelOrderRequest request) {
        String correlationId = MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY);
        log.info("Cancelling order: {}", orderId);

        CancelOrderCommand command = new CancelOrderCommand(orderId, request.reason(), correlationId);
        commandBus.dispatch(command);

        return ResponseEntity.ok(Map.of(
                "orderId", orderId,
                "message", "Order cancelled successfully",
                "correlationId", correlationId != null ? correlationId : ""
        ));
    }
}
