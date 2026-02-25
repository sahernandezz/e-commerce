package com.challenge.ecommercebackend.modules.product.domain.usecase;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;

/**
 * Use case para actualizar el estado de un producto.
 */
public interface UpdateProductStatusUseCase {
    Product execute(Long id, String status);
}

