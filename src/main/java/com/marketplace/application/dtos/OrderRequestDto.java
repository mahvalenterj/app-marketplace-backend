package com.marketplace.application.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class OrderRequestDto {
    @NotBlank(message = "Order number is required")
    private String orderNumber;

    private List<OrderItemRequestDto> items;

    public OrderRequestDto() {
    }

    public OrderRequestDto(String orderNumber, List<OrderItemRequestDto> items) {
        this.orderNumber = orderNumber;
        this.items = items;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<OrderItemRequestDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequestDto> items) {
        this.items = items;
    }
}
