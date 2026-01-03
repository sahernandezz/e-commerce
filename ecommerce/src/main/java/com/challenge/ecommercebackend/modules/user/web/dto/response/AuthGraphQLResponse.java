package com.challenge.ecommercebackend.modules.user.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthGraphQLResponse {
    private String message;
    private String token;
    private UserResponse user;

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResponse {
        private Long id;
        private String email;
        private String name;
        private String role;
    }
}

