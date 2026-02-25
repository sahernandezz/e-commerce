package com.challenge.ecommercebackend.modules.user.domain.usecase;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;

public interface UpdateUserUseCase {
    User execute(Long userId, String name, String email, Integer roleId);
}

