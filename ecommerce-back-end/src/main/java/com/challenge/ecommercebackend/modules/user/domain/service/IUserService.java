package com.challenge.ecommercebackend.modules.user.domain.service;

import com.challenge.ecommercebackend.modules.user.domain.usecase.*;

/**
 * Servicio que agrupa todos los use cases de usuarios.
 */
public interface IUserService {

    // Perfil de usuario
    GetUserProfileUseCase getGetUserProfileUseCase();
    UpdateUserProfileUseCase getUpdateUserProfileUseCase();
    ChangePasswordUseCase getChangePasswordUseCase();

    // Admin - Gestión de usuarios
    GetUsersUseCase getGetUsersUseCase();
    UpdateUserStatusUseCase getUpdateUserStatusUseCase();
}

