package com.challenge.ecommercebackend.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductExternalRequest {
    private String name;
    private Integer price;
    private Integer discount;
}
