package com.marketplace.infrastructure.persistence.repositories;

import com.marketplace.infrastructure.persistence.jpa.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSpringDataRepository extends JpaRepository<ProductJpaEntity, Long> {
    Optional<ProductJpaEntity> findBySku(String sku);
    List<ProductJpaEntity> findByNameContainingIgnoreCase(String name);
}
