package com.marketplace.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long id;
    private String orderNumber;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private List<OrderItem> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order() {
        this.items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.totalPrice = BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Order(String orderNumber) {
        this();
        this.orderNumber = orderNumber;
    }

    public void addItem(OrderItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        this.items.add(item);
        recalculateTotal();
    }

    public void removeItem(OrderItem item) {
        this.items.remove(item);
        recalculateTotal();
    }

    private void recalculateTotal() {
        this.totalPrice = this.items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void confirmOrder() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Only pending orders can be confirmed");
        }
        this.status = OrderStatus.CONFIRMED;
        this.updatedAt = LocalDateTime.now();
    }

    public void shipOrder() {
        if (this.status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Only confirmed orders can be shipped");
        }
        this.status = OrderStatus.SHIPPED;
        this.updatedAt = LocalDateTime.now();
    }

    public void deliverOrder() {
        if (this.status != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Only shipped orders can be delivered");
        }
        this.status = OrderStatus.DELIVERED;
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        recalculateTotal();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public enum OrderStatus {
        PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
    }
}
