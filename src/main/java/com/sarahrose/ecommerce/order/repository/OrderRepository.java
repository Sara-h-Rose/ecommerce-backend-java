package com.sarahrose.ecommerce.order.repository;

import com.sarahrose.ecommerce.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}