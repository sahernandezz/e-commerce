package com.challenge.ecommercebackend.modules.order.domain.usecase;

import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.shared.domain.PageResponse;

import java.util.List;

/**
 * Use case para obtener órdenes (lectura).
 */
public interface GetOrdersUseCase {

    Order getById(Long id);

    List<Order> getByCustomerEmail(String email);

    PageResponse<Order> getAllPaginated(int page, int size, String status, String customerEmail);
}

