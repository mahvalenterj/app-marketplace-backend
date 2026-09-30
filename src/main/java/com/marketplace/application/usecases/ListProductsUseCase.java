package com.marketplace.application.usecases;

import com.marketplace.application.dtos.ProductResponseDto;
import com.marketplace.domain.services.ProductService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListProductsUseCase {
    private final ProductService productService;

    public ListProductsUseCase(ProductService productService) {
        this.productService = productService;
    }

    public List<ProductResponseDto> execute() {
        return productService.listAllProducts().stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
}
