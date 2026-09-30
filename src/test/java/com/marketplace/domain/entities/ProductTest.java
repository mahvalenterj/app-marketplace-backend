package com.marketplace.domain.entities;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void shouldCreateProductWithValidData() {
        Product product = new Product("Notebook", "High-end", new BigDecimal("2500.00"), 10, "SKU123");

        assertEquals("Notebook", product.getName());
        assertEquals("High-end", product.getDescription());
        assertEquals(new BigDecimal("2500.00"), product.getPrice());
        assertEquals(10, product.getQuantity());
        assertEquals("SKU123", product.getSku());
        assertNotNull(product.getCreatedAt());
        assertNotNull(product.getUpdatedAt());
    }

    @Test
    void shouldDecreaseQuantitySuccessfully() {
        Product product = new Product("Notebook", "Desc", new BigDecimal("2500"), 10, "SKU123");

        product.decreaseQuantity(3);

        assertEquals(7, product.getQuantity());
    }

    @Test
    void shouldThrowExceptionWhenDecreasingMoreThanAvailable() {
        Product product = new Product("Notebook", "Desc", new BigDecimal("2500"), 5, "SKU123");

        assertThrows(IllegalArgumentException.class, () -> product.decreaseQuantity(10));
    }

    @Test
    void shouldThrowExceptionWhenDecreasingNegativeAmount() {
        Product product = new Product("Notebook", "Desc", new BigDecimal("2500"), 10, "SKU123");

        assertThrows(IllegalArgumentException.class, () -> product.decreaseQuantity(-5));
    }
}
