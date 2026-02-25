package com.challenge.ecommercebackend.modules.user.domain.usecase;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;

public interface ActivateUserUseCase {
    User execute(Long userId);
}

