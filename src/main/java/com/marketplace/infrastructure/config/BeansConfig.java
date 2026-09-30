package com.marketplace.infrastructure.config;

import com.marketplace.domain.repositories.OrderRepository;
import com.marketplace.domain.repositories.ProductRepository;
import com.marketplace.domain.services.OrderService;
import com.marketplace.domain.services.ProductService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public ProductService productService(ProductRepository productRepository) {
        return new ProductService(productRepository);
    }

    @Bean
    public OrderService orderService(OrderRepository orderRepository, ProductRepository productRepository) {
        return new OrderService(orderRepository, productRepository);
    }
}
