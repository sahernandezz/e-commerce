package com.challenge.ecommercebackend.modules.order.web.controller;

import com.challenge.ecommercebackend.modules.order.domain.service.IOrderService;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.web.dto.request.InputOrderRequest;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @MutationMapping("createOrder")
    public String createOrder(@Argument @Valid InputOrderRequest input) {
        return orderService.createOrder(input);
    }

    @QueryMapping("getMyOrders")
    public List<Order> getMyOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return List.of();
        }
        String userEmail = authentication.getName();
        return orderService.getOrdersByCustomerEmail(userEmail);
    }
}
