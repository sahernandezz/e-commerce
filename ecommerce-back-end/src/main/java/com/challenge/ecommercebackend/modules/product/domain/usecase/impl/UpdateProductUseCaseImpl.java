package com.challenge.ecommercebackend.modules.product.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.product.domain.usecase.UpdateProductUseCase;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.persisten.repository.command.IProductCommandRepository;
import com.challenge.ecommercebackend.modules.product.persisten.repository.query.ICategoryQueryRepository;
import com.challenge.ecommercebackend.modules.product.persisten.repository.query.IProductQueryRepository;
import com.challenge.ecommercebackend.modules.product.web.dto.request.InputProductRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UpdateProductUseCaseImpl implements UpdateProductUseCase {

    private final IProductQueryRepository productQueryRepository;
    private final IProductCommandRepository productCommandRepository;
    private final ICategoryQueryRepository categoryQueryRepository;

    @Override
    @Transactional
    public Product execute(Long id, InputProductRequest request) {
        Product product = productQueryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        if (request.getCategoryId() != null) {
            Category category = categoryQueryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
            product.setCategory(category);
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setDiscount(request.getDiscount());
        product.setImagesUrl(request.getImagesUrl());
        product.setColors(request.getColors());
        product.setSizes(request.getSizes());
        product.setUpdatedAt(new Date());

        return productCommandRepository.save(product);
    }
}
