package com.challenge.ecommercebackend.modules.user.domain.usecase.impl;

import com.challenge.ecommercebackend.config.cqrs.CommandTransactional;
import com.challenge.ecommercebackend.modules.user.domain.usecase.GetUserProfileUseCase;
import com.challenge.ecommercebackend.modules.user.domain.usecase.UpdateUserProfileUseCase;
import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import com.challenge.ecommercebackend.modules.user.persisten.repository.command.IUserCommandRepository;
import com.challenge.ecommercebackend.modules.user.persisten.repository.query.IUserQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@CommandTransactional
public class UpdateUserProfileUseCaseImpl implements UpdateUserProfileUseCase {

    private final IUserCommandRepository userCommandRepository;
    private final IUserQueryRepository userQueryRepository;
    private final GetUserProfileUseCase getUserProfileUseCase;

    @Override
    public User execute(String email, String name, String newEmail) {
        User user = getUserProfileUseCase.execute(email);
        user.setName(name);
        if (!email.equals(newEmail)) {
            if (userQueryRepository.findByEmail(newEmail).isPresent()) {
                throw new IllegalStateException("El email ya esta en uso");
            }
            user.setEmail(newEmail);
        }
        return userCommandRepository.save(user);
    }
}

