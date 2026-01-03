package com.challenge.ecommercebackend.modules.user.domain.service.impl;

import com.challenge.ecommercebackend.modules.user.domain.service.IUserService;

import com.challenge.ecommercebackend.modules.user.persisten.entity.Role;
import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import com.challenge.ecommercebackend.modules.user.persisten.entity.UserStatus;
import com.challenge.ecommercebackend.modules.user.persisten.repository.command.IUserCommandRepository;
import com.challenge.ecommercebackend.modules.user.persisten.repository.query.IRoleQueryRepository;
import com.challenge.ecommercebackend.modules.user.persisten.repository.query.IUserQueryRepository;
import com.challenge.ecommercebackend.modules.user.web.dto.request.ChangePasswordRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserCommandRepository userCommandRepository;
    private final IUserQueryRepository userQueryRepository;
    private final IRoleQueryRepository roleQueryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User getUserProfile(String email) {
        return userQueryRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    @Override
    @Transactional
    public User updateUserProfile(String email, String name, String newEmail) {
        User user = getUserProfile(email);
        user.setName(name);
        if (!email.equals(newEmail)) {
            // Verificar que el nuevo email no exista
            if (userQueryRepository.findByEmail(newEmail).isPresent()) {
                throw new IllegalStateException("El email ya está en uso");
            }
            user.setEmail(newEmail);
        }
        return userCommandRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = getUserProfile(email);

        // Verificar contraseña actual
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("La contraseña actual es incorrecta");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userCommandRepository.save(user);
    }

    @Override
    public Page<User> getAllUsersAdmin(Pageable pageable, String status, String search) {
        // Admin puede ver todos los usuarios incluyendo inactivos
        // Usa commandRepository para acceso a tabla directa
        if (status != null && search != null) {
            return userCommandRepository.findByStatusAndNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    UserStatus.valueOf(status), search, search, pageable);
        } else if (status != null) {
            return userCommandRepository.findByStatus(UserStatus.valueOf(status), pageable);
        } else if (search != null) {
            return userCommandRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    search, search, pageable);
        }
        return userCommandRepository.findAll(pageable);
    }

    @Override
    public User getUserById(Long userId) {
        // Usa queryRepository para lectura (vista materializada)
        return userQueryRepository.findActiveById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }

    @Override
    @Transactional
    public User createUserAdmin(String name, String email, String password, Integer roleId) {
        // Verificar que el email no exista
        if (userQueryRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("El email ya está en uso");
        }

        Role role = roleQueryRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));

        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();

        return userCommandRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUserAdmin(Long userId, String name, String email, Integer roleId) {
        User user = getUserById(userId);
        user.setName(name);

        // Verificar que el nuevo email no exista (si es diferente)
        if (!user.getEmail().equals(email)) {
            if (userQueryRepository.findByEmail(email).isPresent()) {
                throw new IllegalStateException("El email ya está en uso");
            }
            user.setEmail(email);
        }

        if (roleId != null) {
            Role role = roleQueryRepository.findById(roleId)
                    .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado"));
            user.setRole(role);
        }

        return userCommandRepository.save(user);
    }

    @Override
    @Transactional
    public void softDeleteUser(Long userId) {
        User user = getUserById(userId);
        user.setStatus(UserStatus.INACTIVE);
        userCommandRepository.save(user);
    }

    @Override
    @Transactional
    public User activateUser(Long userId) {
        User user = getUserById(userId);
        user.setStatus(UserStatus.ACTIVE);
        return userCommandRepository.save(user);
    }
}

