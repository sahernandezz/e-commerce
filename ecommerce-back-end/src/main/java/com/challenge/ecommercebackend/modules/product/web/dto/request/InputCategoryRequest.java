package com.challenge.ecommercebackend.modules.product.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InputCategoryRequest {

    @NotBlank(message = "El nombre es requerido")
    private String name;
}

