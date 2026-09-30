package com.marketplace.application.usecases;

import com.marketplace.application.dtos.OrderResponseDto;
import com.marketplace.domain.exceptions.DomainException;
import com.marketplace.domain.services.OrderService;
import org.springframework.stereotype.Service;

@Service
public class GetOrderUseCase {
    private final OrderService orderService;

    public GetOrderUseCase(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderResponseDto execute(Long orderId) {
        var order = orderService.getOrderById(orderId)
                .orElseThrow(() -> new DomainException("Order not found", "ORDER_NOT_FOUND"));
        return new OrderResponseDto(order);
    }
}
