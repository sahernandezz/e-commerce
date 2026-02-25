package com.challenge.ecommercebackend.modules.user.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.user.domain.usecase.GetUserProfileUseCase;
import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import com.challenge.ecommercebackend.modules.user.persisten.repository.query.IUserQueryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserProfileUseCaseImpl implements GetUserProfileUseCase {

    private final IUserQueryRepository userQueryRepository;

    @Override
    public User execute(String email) {
        return userQueryRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    }
}

