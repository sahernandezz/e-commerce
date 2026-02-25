package com.challenge.ecommercebackend.modules.user.domain.usecase;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;

public interface GetUserProfileUseCase {
    User execute(String email);
}

