package com.challenge.ecommercebackend.modules.order.domain.usecase;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
public interface GetOrderByIdAndCustomerUseCase {
    Order execute(Long orderId, String email);
}
