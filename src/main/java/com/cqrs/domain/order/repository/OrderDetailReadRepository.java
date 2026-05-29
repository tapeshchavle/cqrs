package com.cqrs.domain.order.repository;

import com.cqrs.domain.order.projection.OrderDetailProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderDetailReadRepository extends JpaRepository<OrderDetailProjection, UUID> {
}
