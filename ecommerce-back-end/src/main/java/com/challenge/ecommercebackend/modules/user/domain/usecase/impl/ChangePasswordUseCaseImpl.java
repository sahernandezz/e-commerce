package com.challenge.ecommercebackend.modules.user.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.user.domain.usecase.ChangePasswordUseCase;
import com.challenge.ecommercebackend.modules.user.domain.usecase.GetUserProfileUseCase;
import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import com.challenge.ecommercebackend.modules.user.persisten.repository.command.IUserCommandRepository;
import com.challenge.ecommercebackend.modules.user.web.dto.request.ChangePasswordRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangePasswordUseCaseImpl implements ChangePasswordUseCase {

    private final IUserCommandRepository userCommandRepository;
    private final GetUserProfileUseCase getUserProfileUseCase;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void execute(String email, ChangePasswordRequest request) {
        User user = getUserProfileUseCase.execute(email);

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("La contrasena actual es incorrecta");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userCommandRepository.save(user);
    }
}

