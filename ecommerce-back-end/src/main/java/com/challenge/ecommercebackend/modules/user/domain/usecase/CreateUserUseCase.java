package com.challenge.ecommercebackend.modules.user.domain.usecase;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;

public interface CreateUserUseCase {
    User execute(String name, String email, String password, Integer roleId);
}

