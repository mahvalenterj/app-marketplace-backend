package com.marketplace.application.usecases;

import com.marketplace.application.dtos.OrderResponseDto;
import com.marketplace.domain.services.OrderService;
import org.springframework.stereotype.Service;

@Service
public class ConfirmOrderUseCase {
    private final OrderService orderService;

    public ConfirmOrderUseCase(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderResponseDto execute(Long orderId) {
        var order = orderService.confirmOrder(orderId);
        return new OrderResponseDto(order);
    }
}
