package com.cqrs.domain.order.projector;

import com.cqrs.domain.order.aggregate.OrderStatus;
import com.cqrs.domain.order.event.*;
import com.cqrs.domain.order.projection.*;
import com.cqrs.domain.order.repository.OrderDetailReadRepository;
import com.cqrs.domain.order.repository.OrderSummaryReadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderProjector {

    private static final Logger log = LoggerFactory.getLogger(OrderProjector.class);
    private final OrderSummaryReadRepository summaryRepository;
    private final OrderDetailReadRepository detailRepository;

    public OrderProjector(OrderSummaryReadRepository summaryRepository,
                          OrderDetailReadRepository detailRepository) {
        this.summaryRepository = summaryRepository;
        this.detailRepository = detailRepository;
    }

    @Async("cqrsEventExecutor")
    @EventListener
    @Transactional
    public void on(OrderCreatedEvent event) {
        log.debug("Projecting OrderCreatedEvent for aggregate: {}", event.getAggregateId());

        OrderSummaryProjection summary = new OrderSummaryProjection(
                event.getAggregateId(), event.getCustomerId(),
                event.getCustomerName(), OrderStatus.CREATED.name()
        );
        summaryRepository.save(summary);

        OrderDetailProjection detail = new OrderDetailProjection(
                event.getAggregateId(), event.getCustomerId(),
                event.getCustomerName(), OrderStatus.CREATED.name()
        );
        detailRepository.save(detail);

        log.info("Projected OrderCreated: orderId={}", event.getAggregateId());
    }

    @Async("cqrsEventExecutor")
    @EventListener
    @Transactional
    public void on(OrderItemAddedEvent event) {
        log.debug("Projecting OrderItemAddedEvent for aggregate: {}", event.getAggregateId());

        // Update summary
        summaryRepository.findById(event.getAggregateId()).ifPresent(summary -> {
            summary.setItemCount(summary.getItemCount() + 1);
            summary.setTotalAmount(summary.getTotalAmount().add(
                    event.getUnitPrice().multiply(java.math.BigDecimal.valueOf(event.getQuantity()))
            ));
            summaryRepository.save(summary);
        });

        // Update detail
        detailRepository.findById(event.getAggregateId()).ifPresent(detail -> {
            OrderItemProjection item = new OrderItemProjection(
                    event.getItemId(), event.getProductId(), event.getProductName(),
                    event.getQuantity(), event.getUnitPrice()
            );
            detail.addItem(item);
            detailRepository.save(detail);
        });

        log.info("Projected OrderItemAdded: orderId={}, itemId={}", event.getAggregateId(), event.getItemId());
    }

    @Async("cqrsEventExecutor")
    @EventListener
    @Transactional
    public void on(OrderItemRemovedEvent event) {
        log.debug("Projecting OrderItemRemovedEvent for aggregate: {}", event.getAggregateId());

        summaryRepository.findById(event.getAggregateId()).ifPresent(summary -> {
            summary.setItemCount(Math.max(0, summary.getItemCount() - 1));
            summaryRepository.save(summary);
        });

        detailRepository.findById(event.getAggregateId()).ifPresent(detail -> {
            detail.removeItem(event.getItemId());
            detailRepository.save(detail);
            // Update summary total from detail
            summaryRepository.findById(event.getAggregateId()).ifPresent(summary -> {
                summary.setTotalAmount(detail.getTotalAmount());
                summaryRepository.save(summary);
            });
        });

        log.info("Projected OrderItemRemoved: orderId={}, itemId={}", event.getAggregateId(), event.getItemId());
    }

    @Async("cqrsEventExecutor")
    @EventListener
    @Transactional
    public void on(OrderConfirmedEvent event) {
        log.debug("Projecting OrderConfirmedEvent for aggregate: {}", event.getAggregateId());

        summaryRepository.findById(event.getAggregateId()).ifPresent(summary -> {
            summary.setStatus(OrderStatus.CONFIRMED.name());
            summary.setTotalAmount(event.getTotalAmount());
            summaryRepository.save(summary);
        });

        detailRepository.findById(event.getAggregateId()).ifPresent(detail -> {
            detail.setStatus(OrderStatus.CONFIRMED.name());
            detail.setTotalAmount(event.getTotalAmount());
            detailRepository.save(detail);
        });

        log.info("Projected OrderConfirmed: orderId={}", event.getAggregateId());
    }

    @Async("cqrsEventExecutor")
    @EventListener
    @Transactional
    public void on(OrderCancelledEvent event) {
        log.debug("Projecting OrderCancelledEvent for aggregate: {}", event.getAggregateId());

        summaryRepository.findById(event.getAggregateId()).ifPresent(summary -> {
            summary.setStatus(OrderStatus.CANCELLED.name());
            summaryRepository.save(summary);
        });

        detailRepository.findById(event.getAggregateId()).ifPresent(detail -> {
            detail.setStatus(OrderStatus.CANCELLED.name());
            detailRepository.save(detail);
        });

        log.info("Projected OrderCancelled: orderId={}", event.getAggregateId());
    }
}
