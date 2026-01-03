package com.challenge.ecommercebackend.modules.product.domain.usecase;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.web.dto.request.InputProductRequest;

/**
 * Use case para actualizar productos.
 */
public interface UpdateProductUseCase {
    Product execute(Long id, InputProductRequest request);
}
