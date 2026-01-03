package com.challenge.ecommercebackend.modules.user.domain.usecase;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import com.challenge.ecommercebackend.shared.domain.PageResponse;

/**
 * Use case para obtener usuarios (lectura).
 */
public interface GetUsersUseCase {

    User getById(Long id);

    User getByEmail(String email);

    PageResponse<User> getAllPaginated(int page, int size, String status, String search);
}

