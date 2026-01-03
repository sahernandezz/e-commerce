package com.challenge.ecommercebackend.modules.order.web.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InputOrderProductRequest {

    @NotNull(message = "quantity is required")
    private Integer quantity;

    @NotNull(message = "idProduct is required")
    private Long productId;

    private String color;

    private String size;
}
