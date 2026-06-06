package com.webtech.saas.mappers;

import com.webtech.saas.entities.Category;
import com.webtech.saas.entities.Product;
import com.webtech.saas.requests.productRequest;
import com.webtech.saas.responses.ProductResponse;

public class ProductMapper {

    public Product toEntity(final productRequest request) {
        return Product.builder()
                .name(request.getName())
                .reference(request.getReference())
                .description(request.getDescription())
                .alertThreshold(request.getAlertThreshold())
                .price(request.getPrice())
                .category(Category.builder()
                        .Id(request.getCategoryId())
                        .build())
                .build();
    }

    public ProductResponse toResponse(final Product product) {
        return ProductResponse.builder()
                .Id(product.getId())
                .name(product.getName())
                .reference(product.getReference())
                .description(product.getDescription())
                .alertThreshold(product.getAlertThreshold())
                .price(product.getPrice())
                .categoryName(product.getCategory().getName())
                .build();
    }
}
