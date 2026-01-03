package com.challenge.ecommercebackend.modules.user.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    @NotBlank(message = "El nombre es requerido")
    private String name;

    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es requerido")
    private String email;
}

