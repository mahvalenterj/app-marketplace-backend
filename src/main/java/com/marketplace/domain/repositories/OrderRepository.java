package com.marketplace.domain.repositories;

import com.marketplace.domain.entities.Order;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    Optional<Order> findByOrderNumber(String orderNumber);
    List<Order> findAll();
    List<Order> findByStatus(Order.OrderStatus status);
    void delete(Long id);
    boolean existsById(Long id);
}
