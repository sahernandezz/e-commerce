package com.challenge.ecommercebackend.modules.user.web.dto.response;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class AuthenticationResponse {
    private String message;

    private User user;

    private String token;
}