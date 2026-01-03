package com.challenge.ecommercebackend.modules.order.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class InputOrderRequest {

    @NotBlank(message = "El correo electrónico es requerido")
    @Email(message = "El correo electrónico debe ser válido")
    private String emailCustomer;

    @NotNull(message = "address is required")
    private String address;

    @NotNull(message = "city is required")
    private String city;

    private String description;

    @NotNull(message = "paymentMethod is required")
    private String paymentMethod;

    @NotNull(message = "orderCode is required")
    List<InputOrderProductRequest> products;
}
