package com.marketplace.infrastructure.persistence.repositories;

import com.marketplace.domain.entities.Product;
import com.marketplace.infrastructure.persistence.jpa.ProductJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryIntegrationTest {

    @Autowired
    private ProductSpringDataRepository springDataRepository;

    @Autowired
    private TestEntityManager entityManager;

    private ProductRepositoryAdapter adapter;
    private ProductJpaEntity savedEntity;

    @BeforeEach
    void setUp() {
        adapter = new ProductRepositoryAdapter(springDataRepository);

        ProductJpaEntity entity = new ProductJpaEntity();
        entity.setName("Notebook");
        entity.setDescription("High-end notebook");
        entity.setPrice(new BigDecimal("2500.00"));
        entity.setQuantity(10);
        entity.setSku("SKU-001");

        savedEntity = entityManager.persistAndFlush(entity);
        entityManager.clear();
    }

    @Test
    void shouldSaveProduct() {
        Product product = new Product("Mouse", "Wireless mouse", new BigDecimal("50.00"), 20, "SKU-002");
        Product saved = adapter.save(product);

        assertNotNull(saved.getId());
        assertEquals("Mouse", saved.getName());
    }

    @Test
    void shouldFindProductById() {
        Optional<Product> found = adapter.findById(savedEntity.getId());

        assertTrue(found.isPresent());
        assertEquals("Notebook", found.get().getName());
        assertEquals(new BigDecimal("2500.00"), found.get().getPrice());
    }

    @Test
    void shouldFindProductBySku() {
        Optional<Product> found = adapter.findBySku("SKU-001");

        assertTrue(found.isPresent());
        assertEquals("Notebook", found.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenSkuNotFound() {
        Optional<Product> found = adapter.findBySku("NONEXISTENT");

        assertFalse(found.isPresent());
    }

    @Test
    void shouldFindAllProducts() {
        ProductJpaEntity entity2 = new ProductJpaEntity();
        entity2.setName("Keyboard");
        entity2.setDescription("Mechanical keyboard");
        entity2.setPrice(new BigDecimal("150.00"));
        entity2.setQuantity(5);
        entity2.setSku("SKU-003");
        entityManager.persistAndFlush(entity2);
        entityManager.clear();

        List<Product> products = adapter.findAll();

        assertTrue(products.size() >= 2);
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Notebook")));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Keyboard")));
    }

    @Test
    void shouldFindProductByName() {
        List<Product> found = adapter.findByName("Notebook");

        assertFalse(found.isEmpty());
        assertTrue(found.stream().anyMatch(p -> p.getName().contains("Notebook")));
    }

    @Test
    void shouldDeleteProduct() {
        Long id = savedEntity.getId();
        adapter.delete(id);

        Optional<Product> found = adapter.findById(id);
        assertFalse(found.isPresent());
    }

    @Test
    void shouldCheckIfProductExists() {
        boolean exists = adapter.existsById(savedEntity.getId());
        assertTrue(exists);

        boolean notExists = adapter.existsById(99999L);
        assertFalse(notExists);
    }

    @Test
    void shouldUpdateProduct() {
        Product product = adapter.findById(savedEntity.getId()).get();
        product.setQuantity(5);

        Product updated = adapter.save(product);

        assertEquals(5, updated.getQuantity());

        Product reloaded = adapter.findById(updated.getId()).get();
        assertEquals(5, reloaded.getQuantity());
    }
}
