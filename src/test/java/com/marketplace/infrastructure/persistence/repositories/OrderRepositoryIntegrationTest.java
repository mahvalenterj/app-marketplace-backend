package com.marketplace.infrastructure.persistence.repositories;

import com.marketplace.domain.entities.Order;
import com.marketplace.infrastructure.persistence.jpa.OrderJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryIntegrationTest {

    @Autowired
    private OrderSpringDataRepository springDataRepository;

    @Autowired
    private TestEntityManager entityManager;

    private OrderRepositoryAdapter adapter;
    private OrderJpaEntity savedEntity;

    @BeforeEach
    void setUp() {
        adapter = new OrderRepositoryAdapter(springDataRepository);

        OrderJpaEntity entity = new OrderJpaEntity();
        entity.setOrderNumber("ORD-001");
        entity.setStatus(OrderJpaEntity.OrderStatus.PENDING);
        entity.setTotalPrice(BigDecimal.ZERO);

        savedEntity = entityManager.persistAndFlush(entity);
        entityManager.clear();
    }

    @Test
    void shouldSaveOrder() {
        Order order = new Order("ORD-002");
        Order saved = adapter.save(order);

        assertNotNull(saved.getId());
        assertEquals("ORD-002", saved.getOrderNumber());
        assertEquals(Order.OrderStatus.PENDING, saved.getStatus());
    }

    @Test
    void shouldFindOrderById() {
        Optional<Order> found = adapter.findById(savedEntity.getId());

        assertTrue(found.isPresent());
        assertEquals("ORD-001", found.get().getOrderNumber());
        assertEquals(Order.OrderStatus.PENDING, found.get().getStatus());
    }

    @Test
    void shouldFindOrderByOrderNumber() {
        Optional<Order> found = adapter.findByOrderNumber("ORD-001");

        assertTrue(found.isPresent());
        assertEquals("ORD-001", found.get().getOrderNumber());
    }

    @Test
    void shouldReturnEmptyWhenOrderNumberNotFound() {
        Optional<Order> found = adapter.findByOrderNumber("NONEXISTENT");

        assertFalse(found.isPresent());
    }

    @Test
    void shouldFindAllOrders() {
        OrderJpaEntity entity2 = new OrderJpaEntity();
        entity2.setOrderNumber("ORD-002");
        entity2.setStatus(OrderJpaEntity.OrderStatus.CONFIRMED);
        entity2.setTotalPrice(new BigDecimal("100.00"));
        entityManager.persistAndFlush(entity2);
        entityManager.clear();

        List<Order> orders = adapter.findAll();

        assertTrue(orders.size() >= 2);
        assertTrue(orders.stream().anyMatch(o -> o.getOrderNumber().equals("ORD-001")));
        assertTrue(orders.stream().anyMatch(o -> o.getOrderNumber().equals("ORD-002")));
    }

    @Test
    void shouldFindOrdersByStatus() {
        OrderJpaEntity confirmed = new OrderJpaEntity();
        confirmed.setOrderNumber("ORD-002");
        confirmed.setStatus(OrderJpaEntity.OrderStatus.CONFIRMED);
        confirmed.setTotalPrice(new BigDecimal("500.00"));
        entityManager.persistAndFlush(confirmed);
        entityManager.clear();

        List<Order> pendingOrders = adapter.findByStatus(Order.OrderStatus.PENDING);
        List<Order> confirmedOrders = adapter.findByStatus(Order.OrderStatus.CONFIRMED);

        assertTrue(pendingOrders.stream().anyMatch(o -> o.getOrderNumber().equals("ORD-001")));
        assertTrue(confirmedOrders.stream().anyMatch(o -> o.getOrderNumber().equals("ORD-002")));
    }

    @Test
    void shouldDeleteOrder() {
        Long id = savedEntity.getId();
        adapter.delete(id);

        Optional<Order> found = adapter.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void shouldCheckIfOrderExists() {
        boolean exists = adapter.existsById(savedEntity.getId());
        assertTrue(exists);

        boolean notExists = adapter.existsById(99999L);
        assertFalse(notExists);
    }

    @Test
    void shouldUpdateOrderStatus() {
        Order order = adapter.findById(savedEntity.getId()).get();
        order.confirmOrder();

        Order updated = adapter.save(order);

        assertEquals(Order.OrderStatus.CONFIRMED, updated.getStatus());

        Order reloaded = adapter.findById(updated.getId()).get();
        assertEquals(Order.OrderStatus.CONFIRMED, reloaded.getStatus());
    }
}
