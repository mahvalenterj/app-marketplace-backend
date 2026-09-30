package com.marketplace.infrastructure.persistence.repositories;

import com.marketplace.domain.entities.Order;
import com.marketplace.domain.entities.OrderItem;
import com.marketplace.domain.repositories.OrderRepository;
import com.marketplace.infrastructure.persistence.jpa.OrderJpaEntity;
import com.marketplace.infrastructure.persistence.jpa.OrderItemJpaEntity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryAdapter implements OrderRepository {
    private final OrderSpringDataRepository springDataRepository;

    public OrderRepositoryAdapter(OrderSpringDataRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Order save(Order order) {
        OrderJpaEntity entity = toPersistenceEntity(order);
        OrderJpaEntity saved = springDataRepository.save(entity);
        return toDomainEntity(saved);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return springDataRepository.findById(id)
                .map(this::toDomainEntity);
    }

    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return springDataRepository.findByOrderNumber(orderNumber)
                .map(this::toDomainEntity);
    }

    @Override
    public List<Order> findAll() {
        return springDataRepository.findAll().stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByStatus(Order.OrderStatus status) {
        OrderJpaEntity.OrderStatus jpaStatus = OrderJpaEntity.OrderStatus.valueOf(status.name());
        return springDataRepository.findByStatus(jpaStatus).stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return springDataRepository.existsById(id);
    }

    private Order toDomainEntity(OrderJpaEntity entity) {
        Order order = new Order(entity.getOrderNumber());
        order.setId(entity.getId());
        order.setStatus(Order.OrderStatus.valueOf(entity.getStatus().name()));
        order.setTotalPrice(entity.getTotalPrice());
        order.setCreatedAt(entity.getCreatedAt());
        order.setUpdatedAt(entity.getUpdatedAt());

        List<OrderItem> items = entity.getItems().stream()
                .map(this::toDomainItem)
                .collect(Collectors.toList());
        order.setItems(items);

        return order;
    }

    private OrderItem toDomainItem(OrderItemJpaEntity entity) {
        OrderItem item = new OrderItem(
                entity.getProductId(),
                entity.getProductName(),
                entity.getQuantity(),
                entity.getUnitPrice()
        );
        item.setId(entity.getId());
        return item;
    }

    private OrderJpaEntity toPersistenceEntity(Order order) {
        OrderJpaEntity entity = new OrderJpaEntity(order.getOrderNumber());
        if (order.getId() != null) {
            entity.setId(order.getId());
        }
        entity.setStatus(OrderJpaEntity.OrderStatus.valueOf(order.getStatus().name()));
        entity.setTotalPrice(order.getTotalPrice());
        if (order.getCreatedAt() != null) {
            entity.setCreatedAt(order.getCreatedAt());
        }
        if (order.getUpdatedAt() != null) {
            entity.setUpdatedAt(order.getUpdatedAt());
        }

        List<OrderItemJpaEntity> items = order.getItems().stream()
                .map(item -> toPersistenceItem(item, entity))
                .collect(Collectors.toList());
        entity.setItems(items);

        return entity;
    }

    private OrderItemJpaEntity toPersistenceItem(OrderItem item, OrderJpaEntity order) {
        OrderItemJpaEntity entity = new OrderItemJpaEntity(
                item.getProductId(),
                item.getProductName(),
                item.getQuantity(),
                item.getUnitPrice()
        );
        if (item.getId() != null) {
            entity.setId(item.getId());
        }
        entity.setOrder(order);
        return entity;
    }
}
