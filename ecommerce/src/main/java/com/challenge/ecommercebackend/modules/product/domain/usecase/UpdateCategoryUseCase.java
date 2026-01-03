package com.challenge.ecommercebackend.modules.product.domain.usecase;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;

/**
 * Use case para actualizar categorías (escritura).
 */
public interface UpdateCategoryUseCase {

    Category execute(Long id, String name);
}

