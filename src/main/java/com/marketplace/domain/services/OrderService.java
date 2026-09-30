package com.marketplace.domain.services;

import com.marketplace.domain.entities.Order;
import com.marketplace.domain.entities.OrderItem;
import com.marketplace.domain.exceptions.DomainException;
import com.marketplace.domain.repositories.OrderRepository;
import com.marketplace.domain.repositories.ProductRepository;
import java.util.List;
import java.util.Optional;

public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Order createOrder(String orderNumber) {
        if (orderNumber == null || orderNumber.isBlank()) {
            throw new DomainException("Order number is required", "ORDER_NUMBER_REQUIRED");
        }

        if (orderRepository.findByOrderNumber(orderNumber).isPresent()) {
            throw new DomainException("Order with number " + orderNumber + " already exists", "ORDER_NUMBER_DUPLICATE");
        }

        Order order = new Order(orderNumber);
        return orderRepository.save(order);
    }

    public Order addItemToOrder(Long orderId, Long productId, Integer quantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DomainException("Order not found", "ORDER_NOT_FOUND"));

        var product = productRepository.findById(productId)
                .orElseThrow(() -> new DomainException("Product not found", "PRODUCT_NOT_FOUND"));

        if (quantity <= 0) {
            throw new DomainException("Quantity must be greater than 0", "INVALID_QUANTITY");
        }

        if (product.getQuantity() < quantity) {
            throw new DomainException("Insufficient product quantity", "INSUFFICIENT_QUANTITY");
        }

        OrderItem item = new OrderItem(productId, product.getName(), quantity, product.getPrice());
        order.addItem(item);

        return orderRepository.save(order);
    }

    public Order removeItemFromOrder(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DomainException("Order not found", "ORDER_NOT_FOUND"));

        OrderItem itemToRemove = order.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new DomainException("Order item not found", "ORDER_ITEM_NOT_FOUND"));

        order.removeItem(itemToRemove);
        return orderRepository.save(order);
    }

    public Order confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DomainException("Order not found", "ORDER_NOT_FOUND"));

        if (order.getItems().isEmpty()) {
            throw new DomainException("Cannot confirm order without items", "ORDER_NO_ITEMS");
        }

        order.confirmOrder();
        return orderRepository.save(order);
    }

    public Order shipOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DomainException("Order not found", "ORDER_NOT_FOUND"));

        order.shipOrder();
        return orderRepository.save(order);
    }

    public Order deliverOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new DomainException("Order not found", "ORDER_NOT_FOUND"));

        order.deliverOrder();
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Optional<Order> getOrderByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    public List<Order> listAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> findOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new DomainException("Order not found", "ORDER_NOT_FOUND");
        }
        orderRepository.delete(id);
    }
}
