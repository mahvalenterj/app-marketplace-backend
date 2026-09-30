package com.marketplace.application.usecases;

import com.marketplace.application.dtos.OrderRequestDto;
import com.marketplace.application.dtos.OrderResponseDto;
import com.marketplace.domain.services.OrderService;
import org.springframework.stereotype.Service;

@Service
public class CreateOrderUseCase {
    private final OrderService orderService;

    public CreateOrderUseCase(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderResponseDto execute(OrderRequestDto request) {
        var order = orderService.createOrder(request.getOrderNumber());

        if (request.getItems() != null && !request.getItems().isEmpty()) {
            for (var item : request.getItems()) {
                order = orderService.addItemToOrder(order.getId(), item.getProductId(), item.getQuantity());
            }
        }

        return new OrderResponseDto(order);
    }
}
