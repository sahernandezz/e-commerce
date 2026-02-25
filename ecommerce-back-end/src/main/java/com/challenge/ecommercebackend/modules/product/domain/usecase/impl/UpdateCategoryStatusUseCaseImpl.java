package com.challenge.ecommercebackend.modules.product.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.product.domain.usecase.UpdateCategoryStatusUseCase;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import com.challenge.ecommercebackend.modules.product.persisten.entity.CategoryStatus;
import com.challenge.ecommercebackend.modules.product.persisten.repository.command.ICategoryCommandRepository;
import com.challenge.ecommercebackend.modules.product.persisten.repository.query.ICategoryQueryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCategoryStatusUseCaseImpl implements UpdateCategoryStatusUseCase {

    private final ICategoryQueryRepository categoryQueryRepository;
    private final ICategoryCommandRepository categoryCommandRepository;

    @Override
    @Transactional
    public Category execute(Long id, String status) {
        Category category = categoryQueryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));

        category.setStatus(CategoryStatus.valueOf(status));

        return categoryCommandRepository.save(category);
    }
}
