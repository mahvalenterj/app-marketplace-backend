package com.marketplace.domain.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void shouldCreateOrderWithPendingStatus() {
        Order order = new Order("ORD-001");

        assertEquals("ORD-001", order.getOrderNumber());
        assertEquals(Order.OrderStatus.PENDING, order.getStatus());
        assertEquals(BigDecimal.ZERO, order.getTotalPrice());
        assertTrue(order.getItems().isEmpty());
    }

    @Test
    void shouldAddItemToOrder() {
        Order order = new Order("ORD-001");
        OrderItem item = new OrderItem(1L, "Notebook", 2, new BigDecimal("2500.00"));

        order.addItem(item);

        assertEquals(1, order.getItems().size());
        assertEquals(new BigDecimal("5000.00"), order.getTotalPrice());
    }

    @Test
    void shouldRemoveItemFromOrder() {
        Order order = new Order("ORD-001");
        OrderItem item = new OrderItem(1L, "Notebook", 2, new BigDecimal("2500.00"));
        order.addItem(item);

        order.removeItem(item);

        assertTrue(order.getItems().isEmpty());
        assertEquals(BigDecimal.ZERO, order.getTotalPrice());
    }

    @Test
    void shouldConfirmOrder() {
        Order order = new Order("ORD-001");
        order.addItem(new OrderItem(1L, "Notebook", 1, new BigDecimal("2500")));

        order.confirmOrder();

        assertEquals(Order.OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenConfirmingNonPendingOrder() {
        Order order = new Order("ORD-001");
        order.setStatus(Order.OrderStatus.CONFIRMED);

        assertThrows(IllegalStateException.class, order::confirmOrder);
    }

    @Test
    void shouldShipOrder() {
        Order order = new Order("ORD-001");
        order.setStatus(Order.OrderStatus.CONFIRMED);

        order.shipOrder();

        assertEquals(Order.OrderStatus.SHIPPED, order.getStatus());
    }

    @Test
    void shouldDeliverOrder() {
        Order order = new Order("ORD-001");
        order.setStatus(Order.OrderStatus.SHIPPED);

        order.deliverOrder();

        assertEquals(Order.OrderStatus.DELIVERED, order.getStatus());
    }
}
