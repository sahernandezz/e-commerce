package com.challenge.ecommercebackend.modules.order.web.dto.response;

import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import com.challenge.ecommercebackend.modules.order.persisten.entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private String orderCode;
    private String emailCustomer;
    private String address;
    private String city;
    private String description;
    private PaymentMethod paymentMethod;
    private OrderStatus status;
    private Integer total;
    private Date createdAt;
    private Date updatedAt;
    private List<OrderProductResponse> products;
}

