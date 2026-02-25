package com.challenge.ecommercebackend.modules.order.domain.usecase;

import com.challenge.ecommercebackend.modules.order.web.dto.request.InputOrderRequest;

public interface CreateOrderUseCase {
    String execute(InputOrderRequest inputOrderRequest);
}

