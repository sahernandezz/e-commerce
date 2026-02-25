package com.challenge.ecommercebackend.modules.user.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.user.domain.usecase.UpdateUserStatusUseCase;
import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import com.challenge.ecommercebackend.modules.user.persisten.entity.UserStatus;
import com.challenge.ecommercebackend.modules.user.persisten.repository.command.IUserCommandRepository;
import com.challenge.ecommercebackend.modules.user.persisten.repository.query.IUserQueryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateUserStatusUseCaseImpl implements UpdateUserStatusUseCase {

    private final IUserQueryRepository userQueryRepository;
    private final IUserCommandRepository userCommandRepository;

    @Override
    @Transactional
    public User execute(Long id, String status) {
        User user = userQueryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        user.setStatus(UserStatus.valueOf(status));
        return userCommandRepository.save(user);
    }
}

