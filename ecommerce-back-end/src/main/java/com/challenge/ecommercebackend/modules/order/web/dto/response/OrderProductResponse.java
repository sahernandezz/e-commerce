package com.challenge.ecommercebackend.modules.order.web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductResponse {
    private Long id;
    private Long productId;
    private String name;
    private Integer quantity;
    private Integer unitPrice;
    private Integer price;
    private Integer total;
    private String size;
    private String color;
    private Integer discount;
    private OrderProductInfoResponse product;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderProductInfoResponse {
        private Long id;
        private String name;
        private List<String> imagesUrl;
    }
}

