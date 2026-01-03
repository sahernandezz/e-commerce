package com.challenge.ecommercebackend.modules.user.web.controller;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import com.challenge.ecommercebackend.modules.user.domain.service.IUserService;
import com.challenge.ecommercebackend.modules.user.web.dto.request.ChangePasswordRequest;
import com.challenge.ecommercebackend.modules.user.web.dto.request.UpdateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestión del perfil de usuario.
 */
@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final IUserService userService;

    /**
     * Obtener perfil del usuario autenticado.
     */
    @GetMapping
    public ResponseEntity<User> getProfile(Authentication authentication) {
        String userEmail = authentication.getName();
        User profile = userService.getUserProfile(userEmail);
        return ResponseEntity.ok(profile);
    }

    /**
     * Actualizar perfil del usuario autenticado.
     */
    @PutMapping
    public ResponseEntity<User> updateProfile(
            @Valid @RequestBody UpdateUserRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        User updatedProfile = userService.updateUserProfile(userEmail, request.getName(), request.getEmail());
        return ResponseEntity.ok(updatedProfile);
    }

    /**
     * Cambiar contraseña del usuario autenticado.
     */
    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        userService.changePassword(userEmail, request);
        return ResponseEntity.ok().build();
    }
}
