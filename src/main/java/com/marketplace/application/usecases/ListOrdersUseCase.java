package com.marketplace.application.usecases;

import com.marketplace.application.dtos.OrderResponseDto;
import com.marketplace.domain.services.OrderService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListOrdersUseCase {
    private final OrderService orderService;

    public ListOrdersUseCase(OrderService orderService) {
        this.orderService = orderService;
    }

    public List<OrderResponseDto> execute() {
        return orderService.listAllOrders().stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());
    }
}
