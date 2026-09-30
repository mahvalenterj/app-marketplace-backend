package com.marketplace.domain.repositories;

import com.marketplace.domain.entities.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    Optional<Product> findBySku(String sku);
    List<Product> findAll();
    List<Product> findByName(String name);
    void delete(Long id);
    boolean existsById(Long id);
}
