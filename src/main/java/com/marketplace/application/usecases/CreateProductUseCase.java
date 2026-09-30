package com.marketplace.application.usecases;

import com.marketplace.application.dtos.ProductRequestDto;
import com.marketplace.application.dtos.ProductResponseDto;
import com.marketplace.domain.services.ProductService;
import org.springframework.stereotype.Service;

@Service
public class CreateProductUseCase {
    private final ProductService productService;

    public CreateProductUseCase(ProductService productService) {
        this.productService = productService;
    }

    public ProductResponseDto execute(ProductRequestDto request) {
        var product = productService.createProduct(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getQuantity(),
                request.getSku()
        );
        return new ProductResponseDto(product);
    }
}
