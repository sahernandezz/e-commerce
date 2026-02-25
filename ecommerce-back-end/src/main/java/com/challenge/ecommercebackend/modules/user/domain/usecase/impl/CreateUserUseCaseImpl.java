package com.challenge.ecommercebackend.modules.user.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.user.domain.usecase.CreateUserUseCase;
import com.challenge.ecommercebackend.modules.user.persisten.entity.Role;
import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import com.challenge.ecommercebackend.modules.user.persisten.entity.UserStatus;
import com.challenge.ecommercebackend.modules.user.persisten.repository.command.IUserCommandRepository;
import com.challenge.ecommercebackend.modules.user.persisten.repository.query.IRoleQueryRepository;
import com.challenge.ecommercebackend.modules.user.persisten.repository.query.IUserQueryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserUseCaseImpl implements CreateUserUseCase {

    private final IUserCommandRepository userCommandRepository;
    private final IUserQueryRepository userQueryRepository;
    private final IRoleQueryRepository roleQueryRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User execute(String name, String email, String password, Integer roleId) {
        if (userQueryRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("El email ya esta en uso");
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
}

