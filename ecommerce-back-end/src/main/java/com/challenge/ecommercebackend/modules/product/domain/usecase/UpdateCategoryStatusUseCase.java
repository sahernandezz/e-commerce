package com.challenge.ecommercebackend.modules.product.domain.usecase;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;

/**
 * Use case para actualizar el estado de una categoría.
 */
public interface UpdateCategoryStatusUseCase {
    Category execute(Long id, String status);
}
