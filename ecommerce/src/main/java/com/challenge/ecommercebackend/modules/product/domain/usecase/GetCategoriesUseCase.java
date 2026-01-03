package com.challenge.ecommercebackend.modules.product.domain.usecase;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import com.challenge.ecommercebackend.shared.domain.PageResponse;

import java.util.List;

/**
 * Use case para obtener categorías (lectura).
 */
public interface GetCategoriesUseCase {

    List<Category> getAllActive();

    Category getById(Long id);

    PageResponse<Category> getAllPaginated(int page, int size, String status, String search);
}

