package com.cqrs.api;

import com.cqrs.domain.order.dto.OrderDetailResponse;
import com.cqrs.domain.order.dto.OrderSummaryResponse;
import com.cqrs.domain.order.query.*;
import com.cqrs.infrastructure.query.QueryBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Maintains all the the get Apis
@RestController
@RequestMapping("/api/orders")
public class OrderQueryController {

    private static final Logger log = LoggerFactory.getLogger(OrderQueryController.class);
    private final QueryBus queryBus;

    public OrderQueryController(QueryBus queryBus) {
        this.queryBus = queryBus;
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrderById(@PathVariable UUID orderId) {
        log.debug("Querying order by id: {}", orderId);
        OrderDetailResponse response = queryBus.dispatch(new GetOrderByIdQuery(orderId));
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "customerId")
    public ResponseEntity<List<OrderSummaryResponse>> getOrdersByCustomer(
            @RequestParam String customerId) {
        log.debug("Querying orders by customer: {}", customerId);
        List<OrderSummaryResponse> response = queryBus.dispatch(new GetOrdersByCustomerQuery(customerId));
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "status")
    public ResponseEntity<List<OrderSummaryResponse>> getOrdersByStatus(
            @RequestParam String status) {
        log.debug("Querying orders by status: {}", status);
        List<OrderSummaryResponse> response = queryBus.dispatch(new GetOrdersByStatusQuery(status));
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrderSummaryResponse>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.debug("Querying all orders: page={}, size={}", page, size);
        Page<OrderSummaryResponse> response = queryBus.dispatch(new GetAllOrdersQuery(page, size));
        return ResponseEntity.ok(response);
    }
}
