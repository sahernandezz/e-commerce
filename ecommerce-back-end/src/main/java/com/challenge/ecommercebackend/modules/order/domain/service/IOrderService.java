package com.challenge.ecommercebackend.modules.order.domain.service;

import com.challenge.ecommercebackend.modules.order.domain.usecase.*;

/**
 * Servicio que agrupa todos los use cases de órdenes.
 */
public interface IOrderService {

    // Use cases de creación
    CreateOrderUseCase getCreateOrderUseCase();

    // Use cases de cliente
    GetOrdersByCustomerUseCase getGetOrdersByCustomerUseCase();
    GetOrderByIdAndCustomerUseCase getGetOrderByIdAndCustomerUseCase();
    CancelOrderByCustomerUseCase getCancelOrderByCustomerUseCase();

    // Use cases de administración
    GetOrdersUseCase getGetOrdersUseCase();
    UpdateOrderStatusUseCase getUpdateOrderStatusUseCase();
}

