package com.challenge.ecommercebackend.modules.product.domain.usecase;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;

/**
 * Use case para crear categorías (escritura).
 */
public interface CreateCategoryUseCase {

    Category execute(String name);
}

