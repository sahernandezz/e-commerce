package com.challenge.ecommercebackend.modules.product.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.product.domain.usecase.CreateProductUseCase;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.persisten.entity.ProductStatus;
import com.challenge.ecommercebackend.modules.product.persisten.repository.command.IProductCommandRepository;
import com.challenge.ecommercebackend.modules.product.persisten.repository.query.ICategoryQueryRepository;
import com.challenge.ecommercebackend.modules.product.web.dto.request.InputProductRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CreateProductUseCaseImpl implements CreateProductUseCase {

    private final IProductCommandRepository productCommandRepository;
    private final ICategoryQueryRepository categoryQueryRepository;

    @Override
    @Transactional
    public Product execute(InputProductRequest request) {
        Category category = categoryQueryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .discount(request.getDiscount())
                .imagesUrl(request.getImagesUrl())
                .colors(request.getColors())
                .sizes(request.getSizes())
                .category(category)
                .status(ProductStatus.ACTIVE)
                .createdAt(new Date())
                .build();

        return productCommandRepository.save(product);
    }
}

