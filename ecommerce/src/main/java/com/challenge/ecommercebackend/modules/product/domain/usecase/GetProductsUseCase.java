package com.challenge.ecommercebackend.modules.product.domain.usecase;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.shared.domain.PageResponse;

import java.util.List;

/**
 * Use case para obtener productos (lectura).
 */
public interface GetProductsUseCase {

    List<Product> getAllActive();

    List<Product> getActiveByCategoryId(Long categoryId);

    List<Product> getActiveByName(String name);

    Product getById(Long id);

    PageResponse<Product> getAllPaginated(int page, int size, String status, String search);
}

