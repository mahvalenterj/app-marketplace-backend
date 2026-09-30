package com.marketplace.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String sku;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Product() {
    }

    public Product(String name, String description, BigDecimal price, Integer quantity, String sku) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.sku = sku;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public void decreaseQuantity(Integer amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.quantity < amount) {
            throw new IllegalArgumentException("Insufficient quantity");
        }
        this.quantity -= amount;
    }
}
