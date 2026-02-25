package com.challenge.ecommercebackend.modules.order.domain.usecase;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
public interface UpdateOrderStatusUseCase {
    Order execute(Long id, String status);
}
