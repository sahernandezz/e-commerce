package com.challenge.ecommercebackend.modules.product.web.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class InputProductRequest {
    @NotNull(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    private Integer price;

    @NotNull(message = "Stock is required")
    private Long categoryId;

    private Integer discount;

    @NotNull(message = "Stock is required")
    private List<String> imagesUrl;

    private List<String> colors;

    private List<String> sizes;
}
