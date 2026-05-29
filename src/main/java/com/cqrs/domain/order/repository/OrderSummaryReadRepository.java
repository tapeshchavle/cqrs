package com.cqrs.domain.order.repository;

import com.cqrs.domain.order.projection.OrderSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderSummaryReadRepository extends JpaRepository<OrderSummaryProjection, UUID> {
    List<OrderSummaryProjection> findByCustomerId(String customerId);
    List<OrderSummaryProjection> findByStatus(String status);
    Page<OrderSummaryProjection> findAll(Pageable pageable);
}
