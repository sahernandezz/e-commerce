package com.challenge.ecommercebackend.modules.user.domain.usecase;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;

/**
 * Use case para actualizar estado de usuarios (escritura).
 */
public interface UpdateUserStatusUseCase {

    User execute(Long id, String status);
}

