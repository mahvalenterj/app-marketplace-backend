package com.marketplace.domain.services;

import com.marketplace.domain.entities.Product;
import com.marketplace.domain.exceptions.DomainException;
import com.marketplace.domain.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository repository;

    private ProductService service;

    @BeforeEach
    void setUp() {
        service = new ProductService(repository);
    }

    @Test
    void shouldCreateProductSuccessfully() {
        when(repository.findBySku("SKU123")).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            p.setId(1L);
            return p;
        });

        Product result = service.createProduct("Notebook", "High-end notebook",
                new BigDecimal("2500.00"), 10, "SKU123");

        assertNotNull(result);
        assertEquals("Notebook", result.getName());
        assertEquals(1L, result.getId());
        verify(repository, times(1)).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCreatingProductWithDuplicateSku() {
        var existingProduct = new Product("Existing", "Desc", new BigDecimal("100"), 5, "SKU123");
        when(repository.findBySku("SKU123")).thenReturn(Optional.of(existingProduct));

        assertThrows(DomainException.class, () ->
            service.createProduct("Notebook", "Desc", new BigDecimal("2500"), 10, "SKU123")
        );
    }

    @Test
    void shouldThrowExceptionWhenCreatingProductWithNullName() {
        assertThrows(DomainException.class, () ->
            service.createProduct(null, "Desc", new BigDecimal("100"), 5, "SKU123")
        );
    }

    @Test
    void shouldThrowExceptionWhenCreatingProductWithInvalidPrice() {
        assertThrows(DomainException.class, () ->
            service.createProduct("Product", "Desc", BigDecimal.ZERO, 5, "SKU123")
        );
    }

    @Test
    void shouldThrowExceptionWhenCreatingProductWithNegativeQuantity() {
        assertThrows(DomainException.class, () ->
            service.createProduct("Product", "Desc", new BigDecimal("100"), -5, "SKU123")
        );
    }

    @Test
    void shouldGetProductById() {
        var product = new Product("Notebook", "Desc", new BigDecimal("2500"), 10, "SKU123");
        product.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(product));

        var result = service.getProductById(1L);

        assertTrue(result.isPresent());
        assertEquals("Notebook", result.get().getName());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> service.updateProduct(999L, "Name", "Desc", new BigDecimal("100"), 5));
    }
}
