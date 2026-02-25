package com.challenge.ecommercebackend.modules.user.domain.usecase;

import com.challenge.ecommercebackend.modules.user.web.dto.request.ChangePasswordRequest;

public interface ChangePasswordUseCase {
    void execute(String email, ChangePasswordRequest request);
}

