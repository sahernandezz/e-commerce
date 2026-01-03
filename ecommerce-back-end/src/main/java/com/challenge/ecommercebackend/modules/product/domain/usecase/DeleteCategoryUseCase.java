package com.challenge.ecommercebackend.modules.product.domain.usecase;

/**
 * Use case para eliminar categorías (soft delete).
 */
public interface DeleteCategoryUseCase {

    void execute(Long id);
}

