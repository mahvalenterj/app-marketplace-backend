package com.marketplace.application.dtos;

import java.math.BigDecimal;

public class ProductRequestDto {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private String sku;

    public ProductRequestDto() {
    }

    public ProductRequestDto(String name, String description, BigDecimal price, Integer quantity, String sku) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.sku = sku;
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
}
