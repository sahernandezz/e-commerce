package com.challenge.ecommercebackend.modules.user.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.user.domain.usecase.UpdateUserUseCase;
import com.challenge.ecommercebackend.modules.user.persisten.entity.Role;
import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import com.challenge.ecommercebackend.modules.user.persisten.repository.command.IUserCommandRepository;
import com.challenge.ecommercebackend.modules.user.persisten.repository.query.IRoleQueryRepository;
import com.challenge.ecommercebackend.modules.user.persisten.repository.query.IUserQueryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCaseImpl implements UpdateUserUseCase {

    private final IUserCommandRepository userCommandRepository;
    private final IUserQueryRepository userQueryRepository;
    private final IRoleQueryRepository roleQueryRepository;

    @Override
    @Transactional
    public User execute(Long userId, String name, String email, Integer roleId) {
        User user = userQueryRepository.findActiveById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        user.setName(name);

        if (!user.getEmail().equals(email)) {
            if (userQueryRepository.findByEmail(email).isPresent()) {
                throw new IllegalStateException("El email ya esta en uso");
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
}

