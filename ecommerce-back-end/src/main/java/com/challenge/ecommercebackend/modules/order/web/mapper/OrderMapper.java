package com.challenge.ecommercebackend.modules.order.web.mapper;

import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.ProductOrder;
import com.challenge.ecommercebackend.modules.order.web.dto.response.OrderProductResponse;
import com.challenge.ecommercebackend.modules.order.web.dto.response.OrderResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        if (order == null) return null;

        List<OrderProductResponse> products = null;
        if (order.getProducts() != null) {
            products = order.getProducts().stream()
                    .map(this::toProductResponse)
                    .collect(Collectors.toList());
        }

        return OrderResponse.builder()
                .id(order.getId())
                .orderCode(order.getOrderCode())
                .emailCustomer(order.getEmailCustomer())
                .address(order.getAddress())
                .city(order.getCity())
                .description(order.getDescription())
                .paymentMethod(order.getPaymentMethod() != null ? order.getPaymentMethod().name() : null)
                .status(order.getStatus() != null ? order.getStatus().name() : null)
                .total(order.getTotal())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .products(products)
                .build();
    }

    public List<OrderResponse> toResponseList(List<Order> orders) {
        if (orders == null) return List.of();
        return orders.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public OrderProductResponse toProductResponse(ProductOrder productOrder) {
        if (productOrder == null) return null;

        OrderProductResponse.OrderProductInfoResponse productInfo = null;
        if (productOrder.getIdProduct() != null) {
            productInfo = OrderProductResponse.OrderProductInfoResponse.builder()
                    .id(productOrder.getIdProduct())
                    .name(productOrder.getName())
                    .imagesUrl(List.of())
                    .build();
        }

        return OrderProductResponse.builder()
                .id(productOrder.getId())
                .productId(productOrder.getIdProduct())
                .name(productOrder.getName())
                .quantity(productOrder.getQuantity())
                .unitPrice(productOrder.getUnitPrice())
                .price(productOrder.getUnitPrice())
                .total(productOrder.getTotal())
                .size(productOrder.getSize())
                .color(productOrder.getColor())
                .discount(productOrder.getDiscount())
                .product(productInfo)
                .build();
    }
}

