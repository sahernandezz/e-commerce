package com.challenge.ecommercebackend.modules.order.service;

import com.challenge.ecommercebackend.modules.order.web.dto.request.InputOrderRequest;

public interface IOrderService {
    String createOrder(InputOrderRequest inputOrderRequest);
}
