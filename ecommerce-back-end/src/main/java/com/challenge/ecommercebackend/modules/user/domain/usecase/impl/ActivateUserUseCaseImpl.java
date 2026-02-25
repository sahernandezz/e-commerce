package com.challenge.ecommercebackend.modules.user.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.user.domain.usecase.ActivateUserUseCase;
import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import com.challenge.ecommercebackend.modules.user.persisten.entity.UserStatus;
import com.challenge.ecommercebackend.modules.user.persisten.repository.command.IUserCommandRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivateUserUseCaseImpl implements ActivateUserUseCase {

    private final IUserCommandRepository userCommandRepository;

    @Override
    @Transactional
    public User execute(Long userId) {
        User user = userCommandRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        user.setStatus(UserStatus.ACTIVE);
        return userCommandRepository.save(user);
    }
}

