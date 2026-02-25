package com.challenge.ecommercebackend.modules.order.web.controller;

import com.challenge.ecommercebackend.modules.order.domain.service.IOrderService;
import com.challenge.ecommercebackend.modules.order.web.dto.request.InputOrderRequest;
import com.challenge.ecommercebackend.modules.order.web.dto.response.OrderResponse;
import com.challenge.ecommercebackend.modules.order.web.mapper.OrderMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final OrderMapper orderMapper;

    @MutationMapping("createOrder")
    public String createOrder(@Argument @Valid InputOrderRequest input) {
        return orderService.getCreateOrderUseCase().execute(input);
    }

    @QueryMapping("getMyOrders")
    public List<OrderResponse> getMyOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return List.of();
        }
        String userEmail = authentication.getName();
        return orderMapper.toResponseList(orderService.getGetOrdersByCustomerUseCase().execute(userEmail));
    }
}
