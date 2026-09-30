package com.marketplace.domain.services;

import com.marketplace.domain.entities.Order;
import com.marketplace.domain.entities.Product;
import com.marketplace.domain.exceptions.DomainException;
import com.marketplace.domain.repositories.OrderRepository;
import com.marketplace.domain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    private OrderService service;

    @BeforeEach
    void setUp() {
        service = new OrderService(orderRepository, productRepository);
    }

    @Test
    void shouldCreateOrderSuccessfully() {
        when(orderRepository.findByOrderNumber("ORD-001")).thenReturn(Optional.empty());
        when(orderRepository.save(any())).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(1L);
            return o;
        });

        Order result = service.createOrder("ORD-001");

        assertNotNull(result);
        assertEquals("ORD-001", result.getOrderNumber());
        assertEquals(Order.OrderStatus.PENDING, result.getStatus());
        verify(orderRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCreatingOrderWithDuplicateNumber() {
        var existingOrder = new Order("ORD-001");
        when(orderRepository.findByOrderNumber("ORD-001")).thenReturn(Optional.of(existingOrder));

        assertThrows(DomainException.class, () -> service.createOrder("ORD-001"));
    }

    @Test
    void shouldThrowExceptionWhenCreatingOrderWithNullNumber() {
        assertThrows(DomainException.class, () -> service.createOrder(null));
    }

    @Test
    void shouldAddItemToOrderSuccessfully() {
        var order = new Order("ORD-001");
        order.setId(1L);
        var product = new Product("Notebook", "Desc", new BigDecimal("2500"), 10, "SKU123");
        product.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any())).thenReturn(order);

        Order result = service.addItemToOrder(1L, 1L, 2);

        assertEquals(1, result.getItems().size());
        assertEquals(2, result.getItems().get(0).getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenAddingItemWithInsufficientQuantity() {
        var order = new Order("ORD-001");
        order.setId(1L);
        var product = new Product("Notebook", "Desc", new BigDecimal("2500"), 5, "SKU123");
        product.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(DomainException.class, () -> service.addItemToOrder(1L, 1L, 10));
    }

    @Test
    void shouldConfirmOrderSuccessfully() {
        var order = new Order("ORD-001");
        order.setId(1L);
        order.addItem(new com.marketplace.domain.entities.OrderItem(1L, "Notebook", 2, new BigDecimal("2500")));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        Order result = service.confirmOrder(1L);

        assertEquals(Order.OrderStatus.CONFIRMED, result.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenConfirmingEmptyOrder() {
        var order = new Order("ORD-001");
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(DomainException.class, () -> service.confirmOrder(1L));
    }

    @Test
    void shouldShipOrderSuccessfully() {
        var order = new Order("ORD-001");
        order.setId(1L);
        order.setStatus(Order.OrderStatus.CONFIRMED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        Order result = service.shipOrder(1L);

        assertEquals(Order.OrderStatus.SHIPPED, result.getStatus());
    }
}
