package com.challenge.ecommercebackend.modules.order.domain.service.impl;

import com.challenge.ecommercebackend.modules.order.domain.service.IOrderService;
import com.challenge.ecommercebackend.modules.order.domain.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio que agrupa todos los use cases de órdenes.
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrdersByCustomerUseCase getOrdersByCustomerUseCase;
    private final GetOrderByIdAndCustomerUseCase getOrderByIdAndCustomerUseCase;
    private final CancelOrderByCustomerUseCase cancelOrderByCustomerUseCase;
    private final GetOrdersUseCase getOrdersUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @Override
    public CreateOrderUseCase getCreateOrderUseCase() {
        return createOrderUseCase;
    }

    @Override
    public GetOrdersByCustomerUseCase getGetOrdersByCustomerUseCase() {
        return getOrdersByCustomerUseCase;
    }

    @Override
    public GetOrderByIdAndCustomerUseCase getGetOrderByIdAndCustomerUseCase() {
        return getOrderByIdAndCustomerUseCase;
    }

    @Override
    public CancelOrderByCustomerUseCase getCancelOrderByCustomerUseCase() {
        return cancelOrderByCustomerUseCase;
    }

    @Override
    public GetOrdersUseCase getGetOrdersUseCase() {
        return getOrdersUseCase;
    }

    @Override
    public UpdateOrderStatusUseCase getUpdateOrderStatusUseCase() {
        return updateOrderStatusUseCase;
    }
}

