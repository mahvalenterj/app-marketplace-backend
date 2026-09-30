package com.marketplace.api.controllers;

import com.marketplace.application.dtos.OrderRequestDto;
import com.marketplace.application.dtos.OrderResponseDto;
import com.marketplace.application.usecases.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class OrderController {
    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final ListOrdersUseCase listOrdersUseCase;
    private final ConfirmOrderUseCase confirmOrderUseCase;

    public OrderController(CreateOrderUseCase createOrderUseCase,
                         GetOrderUseCase getOrderUseCase,
                         ListOrdersUseCase listOrdersUseCase,
                         ConfirmOrderUseCase confirmOrderUseCase) {
        this.createOrderUseCase = createOrderUseCase;
        this.getOrderUseCase = getOrderUseCase;
        this.listOrdersUseCase = listOrdersUseCase;
        this.confirmOrderUseCase = confirmOrderUseCase;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto request) {
        OrderResponseDto response = createOrderUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long id) {
        OrderResponseDto response = getOrderUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> listOrders() {
        List<OrderResponseDto> orders = listOrdersUseCase.execute();
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{id}/confirmar")
    public ResponseEntity<OrderResponseDto> confirmOrder(@PathVariable Long id) {
        OrderResponseDto response = confirmOrderUseCase.execute(id);
        return ResponseEntity.ok(response);
    }
}
