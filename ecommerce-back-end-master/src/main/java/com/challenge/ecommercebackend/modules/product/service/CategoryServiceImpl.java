package com.challenge.ecommercebackend.modules.product.service;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import com.challenge.ecommercebackend.modules.product.persisten.repository.command.ICategoryCommandRepository;
import com.challenge.ecommercebackend.modules.product.persisten.repository.query.ICategoryQueryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryCommandRepository categoryCommandRepository;
    private final ICategoryQueryRepository categoryQueryRepository;

    public CategoryServiceImpl(ICategoryCommandRepository categoryCommandRepository,
                              ICategoryQueryRepository categoryQueryRepository) {
        this.categoryCommandRepository = categoryCommandRepository;
        this.categoryQueryRepository = categoryQueryRepository;
    }

    @Override
    public List<Category> getAllCategoriesActive() {
        return categoryQueryRepository.findAllActive();
    }
}

