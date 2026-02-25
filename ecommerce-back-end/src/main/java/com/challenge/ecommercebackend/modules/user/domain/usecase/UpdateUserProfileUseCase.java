package com.challenge.ecommercebackend.modules.user.domain.usecase;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;

public interface UpdateUserProfileUseCase {
    User execute(String email, String name, String newEmail);
}

