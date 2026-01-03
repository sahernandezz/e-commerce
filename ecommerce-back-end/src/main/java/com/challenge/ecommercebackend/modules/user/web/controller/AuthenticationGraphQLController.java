package com.challenge.ecommercebackend.modules.user.web.controller;

import com.challenge.ecommercebackend.modules.user.domain.service.IAuthenticationService;
import com.challenge.ecommercebackend.modules.user.web.dto.request.AuthRequest;
import com.challenge.ecommercebackend.modules.user.web.dto.request.LoginInput;
import com.challenge.ecommercebackend.modules.user.web.dto.request.RegisterInput;
import com.challenge.ecommercebackend.modules.user.web.dto.response.AuthenticationResponse;
import com.challenge.ecommercebackend.modules.user.web.dto.response.AuthGraphQLResponse;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class AuthenticationGraphQLController {

    private final IAuthenticationService authenticationService;

    public AuthenticationGraphQLController(IAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @MutationMapping("login")
    public AuthGraphQLResponse login(@Argument("input") LoginInput input) {
        AuthRequest authRequest = new AuthRequest(input.getPassword(), input.getEmail());
        Optional<AuthenticationResponse> response = authenticationService.authenticate(authRequest);

        if (response.isPresent()) {
            AuthenticationResponse authResponse = response.get();
            return AuthGraphQLResponse.builder()
                    .message(authResponse.getMessage())
                    .token(authResponse.getToken())
                    .user(AuthGraphQLResponse.UserResponse.builder()
                            .id(authResponse.getUser().getId())
                            .email(authResponse.getUser().getEmail())
                            .name(authResponse.getUser().getName())
                            .role(authResponse.getUser().getRole() != null ? authResponse.getUser().getRole().getName() : null)
                            .build())
                    .build();
        }

        throw new RuntimeException("Usuario o contraseña incorrectos");
    }

    @MutationMapping("register")
    public AuthGraphQLResponse register(@Argument("input") RegisterInput input) {
        AuthenticationResponse authResponse = authenticationService.register(input);

        return AuthGraphQLResponse.builder()
                .message(authResponse.getMessage())
                .token(authResponse.getToken())
                .user(AuthGraphQLResponse.UserResponse.builder()
                        .id(authResponse.getUser().getId())
                        .email(authResponse.getUser().getEmail())
                        .name(authResponse.getUser().getName())
                        .role(authResponse.getUser().getRole() != null ? authResponse.getUser().getRole().getName() : null)
                        .build())
                .build();
    }
}

