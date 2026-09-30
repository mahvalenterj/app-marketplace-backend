package com.marketplace.domain.entities;

import java.math.BigDecimal;

public class OrderItem {
    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;

    public OrderItem() {
    }

    public OrderItem(Long productId, String productName, Integer quantity, BigDecimal unitPrice) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = calculateSubtotal();
    }

    private BigDecimal calculateSubtotal() {
        return unitPrice.multiply(new BigDecimal(quantity));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        this.quantity = quantity;
        this.subtotal = calculateSubtotal();
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        if (unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative");
        }
        this.unitPrice = unitPrice;
        this.subtotal = calculateSubtotal();
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}
