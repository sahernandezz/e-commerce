package com.challenge.ecommercebackend.modules.order.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class InputOrderRequest {

    @NotNull(message = "emailCustomer is required")
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
