package com.cqrs.domain.order.repository;

import com.cqrs.domain.order.aggregate.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderWriteRepository extends JpaRepository<Order, UUID> {
}
