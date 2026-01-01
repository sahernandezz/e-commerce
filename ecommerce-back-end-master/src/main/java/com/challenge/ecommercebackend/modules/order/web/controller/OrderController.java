package com.challenge.ecommercebackend.modules.order.web.controller;

import com.challenge.ecommercebackend.modules.order.service.IOrderService;
import com.challenge.ecommercebackend.modules.order.web.dto.request.InputOrderRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class OrderController {

    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @MutationMapping("createOrder")
    public String createOrder(@Argument InputOrderRequest input) {
        return orderService.createOrder(input);
    }
}
