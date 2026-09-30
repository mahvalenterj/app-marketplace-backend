package com.marketplace.application.usecases;

import com.marketplace.application.dtos.ProductResponseDto;
import com.marketplace.domain.exceptions.DomainException;
import com.marketplace.domain.services.ProductService;
import org.springframework.stereotype.Service;

@Service
public class GetProductUseCase {
    private final ProductService productService;

    public GetProductUseCase(ProductService productService) {
        this.productService = productService;
    }

    public ProductResponseDto execute(Long productId) {
        var product = productService.getProductById(productId)
                .orElseThrow(() -> new DomainException("Product not found", "PRODUCT_NOT_FOUND"));
        return new ProductResponseDto(product);
    }
}
