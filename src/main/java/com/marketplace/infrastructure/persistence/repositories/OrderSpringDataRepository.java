package com.marketplace.infrastructure.persistence.repositories;

import com.marketplace.infrastructure.persistence.jpa.OrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderSpringDataRepository extends JpaRepository<OrderJpaEntity, Long> {
    Optional<OrderJpaEntity> findByOrderNumber(String orderNumber);
    List<OrderJpaEntity> findByStatus(OrderJpaEntity.OrderStatus status);
}
