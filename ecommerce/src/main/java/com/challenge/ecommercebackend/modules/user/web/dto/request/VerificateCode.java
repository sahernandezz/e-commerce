package com.challenge.ecommercebackend.modules.user.web.dto.request;

import com.challenge.ecommercebackend.modules.user.web.validation.Password;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VerificateCode {

    @NotBlank(message = "El código no puede estar vacío")
    private String code;

    @NotNull(message = "Falta un parámetro")
    private Long id;

    @Password(value = 8, message = "La contraseña debe tener al menos " + 8 + " caracteres, una mayúscula, una minúscula, un número y un carácter especial (!@#$%^&*()_+)")
    private String password;
}
