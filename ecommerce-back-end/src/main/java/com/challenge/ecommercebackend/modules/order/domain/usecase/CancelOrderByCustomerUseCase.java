package com.challenge.ecommercebackend.modules.order.domain.usecase;
public interface CancelOrderByCustomerUseCase {
    void execute(Long orderId, String email);
}
