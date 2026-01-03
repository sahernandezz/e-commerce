package com.challenge.ecommercebackend.modules.user.domain.service;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import com.challenge.ecommercebackend.modules.user.web.dto.request.AuthRequest;
import com.challenge.ecommercebackend.modules.user.web.dto.request.RegisterInput;
import com.challenge.ecommercebackend.modules.user.web.dto.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface IAuthenticationService {
    Optional<AuthenticationResponse> authenticate(AuthRequest request);

    AuthenticationResponse register(RegisterInput request);

    String recoverPassword(String correo, HttpServletRequest request);

    Optional<User> verificateRecoveryCode(String recoveryCode, Long id);

    Optional<User> changePassword(String recoveryCode, Long id, String clave);
}
