package com.marketplace.infrastructure.persistence.repositories;

import com.marketplace.domain.entities.Product;
import com.marketplace.domain.repositories.ProductRepository;
import com.marketplace.infrastructure.persistence.jpa.ProductJpaEntity;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryAdapter implements ProductRepository {
    private final ProductSpringDataRepository springDataRepository;

    public ProductRepositoryAdapter(ProductSpringDataRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = toPersistenceEntity(product);
        ProductJpaEntity saved = springDataRepository.save(entity);
        return toDomainEntity(saved);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return springDataRepository.findById(id)
                .map(this::toDomainEntity);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return springDataRepository.findBySku(sku)
                .map(this::toDomainEntity);
    }

    @Override
    public List<Product> findAll() {
        return springDataRepository.findAll().stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByName(String name) {
        return springDataRepository.findByNameContainingIgnoreCase(name).stream()
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

    private Product toDomainEntity(ProductJpaEntity entity) {
        Product product = new Product(
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getQuantity(),
                entity.getSku()
        );
        product.setId(entity.getId());
        product.setCreatedAt(entity.getCreatedAt());
        product.setUpdatedAt(entity.getUpdatedAt());
        return product;
    }

    private ProductJpaEntity toPersistenceEntity(Product product) {
        ProductJpaEntity entity = new ProductJpaEntity(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getSku()
        );
        if (product.getId() != null) {
            entity.setId(product.getId());
        }
        if (product.getCreatedAt() != null) {
            entity.setCreatedAt(product.getCreatedAt());
        }
        if (product.getUpdatedAt() != null) {
            entity.setUpdatedAt(product.getUpdatedAt());
        }
        return entity;
    }
}
