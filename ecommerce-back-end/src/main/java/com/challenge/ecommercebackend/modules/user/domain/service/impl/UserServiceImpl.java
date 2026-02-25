package com.challenge.ecommercebackend.modules.user.domain.service.impl;

import com.challenge.ecommercebackend.modules.user.domain.service.IUserService;
import com.challenge.ecommercebackend.modules.user.domain.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio que agrupa todos los use cases de usuarios.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final GetUserProfileUseCase getUserProfileUseCase;
    private final UpdateUserProfileUseCase updateUserProfileUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final GetUsersUseCase getUsersUseCase;
    private final UpdateUserStatusUseCase updateUserStatusUseCase;

    @Override
    public GetUserProfileUseCase getGetUserProfileUseCase() {
        return getUserProfileUseCase;
    }

    @Override
    public UpdateUserProfileUseCase getUpdateUserProfileUseCase() {
        return updateUserProfileUseCase;
    }

    @Override
    public ChangePasswordUseCase getChangePasswordUseCase() {
        return changePasswordUseCase;
    }

    @Override
    public GetUsersUseCase getGetUsersUseCase() {
        return getUsersUseCase;
    }

    @Override
    public UpdateUserStatusUseCase getUpdateUserStatusUseCase() {
        return updateUserStatusUseCase;
    }
}

