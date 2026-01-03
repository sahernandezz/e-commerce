package com.challenge.ecommercebackend.modules.user.domain.service;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import com.challenge.ecommercebackend.modules.user.web.dto.request.ChangePasswordRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {

    // Perfil de usuario
    User getUserProfile(String email);
    User updateUserProfile(String email, String name, String newEmail);
    void changePassword(String email, ChangePasswordRequest request);

    // Admin - Gestión de usuarios
    Page<User> getAllUsersAdmin(Pageable pageable, String status, String search);
    User getUserById(Long userId);
    User createUserAdmin(String name, String email, String password, Integer roleId);
    User updateUserAdmin(Long userId, String name, String email, Integer roleId);
    void softDeleteUser(Long userId);
    User activateUser(Long userId);
}

