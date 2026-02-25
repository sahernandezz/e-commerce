package com.challenge.ecommercebackend.modules.product.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.product.domain.usecase.GetCategoriesUseCase;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import com.challenge.ecommercebackend.modules.product.persisten.entity.CategoryStatus;
import com.challenge.ecommercebackend.modules.product.persisten.repository.query.ICategoryQueryRepository;
import com.challenge.ecommercebackend.shared.domain.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCategoriesUseCaseImpl implements GetCategoriesUseCase {

    private final ICategoryQueryRepository categoryQueryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllActive() {
        return categoryQueryRepository.findAllActive();
    }

    @Override
    @Transactional(readOnly = true)
    public Category getById(Long id) {
        return categoryQueryRepository.findActiveById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<Category> getAllPaginated(int page, int size, String status, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<Category> categoryPage;

        if (status != null && search != null) {
            categoryPage = categoryQueryRepository.findByStatusAndNameContainingIgnoreCase(
                    CategoryStatus.valueOf(status), search, pageable);
        } else if (status != null) {
            categoryPage = categoryQueryRepository.findByStatus(CategoryStatus.valueOf(status), pageable);
        } else if (search != null) {
            categoryPage = categoryQueryRepository.findByNameContainingIgnoreCase(search, pageable);
        } else {
            categoryPage = categoryQueryRepository.findAll(pageable);
        }

        return PageResponse.<Category>builder()
                .content(categoryPage.getContent())
                .page(categoryPage.getNumber())
                .size(categoryPage.getSize())
                .totalElements(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .first(categoryPage.isFirst())
                .last(categoryPage.isLast())
                .hasNext(categoryPage.hasNext())
                .hasPrevious(categoryPage.hasPrevious())
                .build();
    }
}

