package com.challenge.ecommercebackend.modules.order.domain.usecase;

import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;

/**
 * Use case para actualizar estado de órdenes (escritura).
 */
public interface UpdateOrderStatusUseCase {

    Order execute(Long id, String status);
}

