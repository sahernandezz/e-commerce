package com.challenge.ecommercebackend.modules.product.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.product.domain.usecase.DeleteCategoryUseCase;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import com.challenge.ecommercebackend.modules.product.persisten.entity.CategoryStatus;
import com.challenge.ecommercebackend.modules.product.persisten.repository.command.ICategoryCommandRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCaseImpl implements DeleteCategoryUseCase {

    private final ICategoryCommandRepository categoryCommandRepository;

    @Override
    @Transactional
    public void execute(Long id) {
        Category category = categoryCommandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));

        category.setStatus(CategoryStatus.INACTIVE);
        categoryCommandRepository.save(category);
    }
}

