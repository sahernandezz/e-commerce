package com.challenge.ecommercebackend.modules.product.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.product.domain.usecase.CreateCategoryUseCase;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import com.challenge.ecommercebackend.modules.product.persisten.entity.CategoryStatus;
import com.challenge.ecommercebackend.modules.product.persisten.repository.command.ICategoryCommandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCaseImpl implements CreateCategoryUseCase {

    private final ICategoryCommandRepository categoryCommandRepository;

    @Override
    @Transactional
    public Category execute(String name) {
        if (categoryCommandRepository.findByName(name).isPresent()) {
            throw new IllegalStateException("Ya existe una categoría con ese nombre");
        }

        Category category = Category.builder()
                .name(name)
                .status(CategoryStatus.ACTIVE)
                .build();

        return categoryCommandRepository.save(category);
    }
}

