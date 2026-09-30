package com.marketplace.domain.services;

import com.marketplace.domain.entities.Product;
import com.marketplace.domain.exceptions.DomainException;
import com.marketplace.domain.repositories.ProductRepository;
import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Product createProduct(String name, String description, java.math.BigDecimal price, Integer quantity, String sku) {
        validateProductData(name, description, price, quantity, sku);

        if (repository.findBySku(sku).isPresent()) {
            throw new DomainException("Product with SKU " + sku + " already exists", "PRODUCT_SKU_DUPLICATE");
        }

        Product product = new Product(name, description, price, quantity, sku);
        return repository.save(product);
    }

    public Product updateProduct(Long id, String name, String description, java.math.BigDecimal price, Integer quantity) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new DomainException("Product not found", "PRODUCT_NOT_FOUND"));

        if (name != null && !name.isBlank()) {
            product.setName(name);
        }
        if (description != null && !description.isBlank()) {
            product.setDescription(description);
        }
        if (price != null && price.compareTo(java.math.BigDecimal.ZERO) > 0) {
            product.setPrice(price);
        }
        if (quantity != null && quantity >= 0) {
            product.setQuantity(quantity);
        }

        product.setUpdatedAt(java.time.LocalDateTime.now());
        return repository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return repository.findById(id);
    }

    public List<Product> listAllProducts() {
        return repository.findAll();
    }

    public List<Product> searchByName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Search name cannot be empty", "INVALID_SEARCH");
        }
        return repository.findByName(name);
    }

    public void deleteProduct(Long id) {
        if (!repository.existsById(id)) {
            throw new DomainException("Product not found", "PRODUCT_NOT_FOUND");
        }
        repository.delete(id);
    }

    public void decreaseProductQuantity(Long productId, Integer quantity) {
        Product product = repository.findById(productId)
                .orElseThrow(() -> new DomainException("Product not found", "PRODUCT_NOT_FOUND"));

        product.decreaseQuantity(quantity);
        repository.save(product);
    }

    private void validateProductData(String name, String description, java.math.BigDecimal price, Integer quantity, String sku) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Product name is required", "PRODUCT_NAME_REQUIRED");
        }
        if (description == null || description.isBlank()) {
            throw new DomainException("Product description is required", "PRODUCT_DESCRIPTION_REQUIRED");
        }
        if (price == null || price.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new DomainException("Product price must be greater than zero", "PRODUCT_PRICE_INVALID");
        }
        if (quantity == null || quantity < 0) {
            throw new DomainException("Product quantity cannot be negative", "PRODUCT_QUANTITY_INVALID");
        }
        if (sku == null || sku.isBlank()) {
            throw new DomainException("Product SKU is required", "PRODUCT_SKU_REQUIRED");
        }
    }
}
