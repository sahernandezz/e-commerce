package com.challenge.ecommercebackend.modules.user.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthRequest {
    @NotBlank(message = "La clave no puede ser vacio")
    private String password;

    @NotBlank(message = "El correo no puede ser vacio")
    private String email;
}