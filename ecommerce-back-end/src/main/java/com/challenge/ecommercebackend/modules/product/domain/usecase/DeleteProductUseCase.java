package com.challenge.ecommercebackend.modules.product.domain.usecase;

/**
 * Use case para eliminar productos (soft delete).
 */
public interface DeleteProductUseCase {

    void execute(Long id);
}

