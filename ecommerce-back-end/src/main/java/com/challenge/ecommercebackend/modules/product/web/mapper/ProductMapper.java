package com.challenge.ecommercebackend.modules.product.web.mapper;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.web.dto.response.CategoryResponse;
import com.challenge.ecommercebackend.modules.product.web.dto.response.ProductResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        if (product == null) return null;

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .status(product.getStatus() != null ? product.getStatus().name() : null)
                .category(product.getCategory() != null ? toCategoryResponse(product.getCategory()) : null)
                .imagesUrl(product.getImagesUrl())
                .colors(product.getColors())
                .sizes(product.getSizes())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public List<ProductResponse> toResponseList(List<Product> products) {
        if (products == null) return List.of();
        return products.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CategoryResponse toCategoryResponse(Category category) {
        if (category == null) return null;

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .status(category.getStatus() != null ? category.getStatus().name() : null)
                .build();
    }

    public List<CategoryResponse> toCategoryResponseList(List<Category> categories) {
        if (categories == null) return List.of();
        return categories.stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }
}

