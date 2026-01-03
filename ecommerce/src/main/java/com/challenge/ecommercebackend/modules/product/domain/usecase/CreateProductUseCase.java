package com.challenge.ecommercebackend.modules.product.domain.usecase;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.web.dto.request.InputProductRequest;

/**
 * Use case para crear productos.
 */
public interface CreateProductUseCase {
    Product execute(InputProductRequest request);
}

