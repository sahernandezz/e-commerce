package com.challenge.ecommercebackend.modules.order.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.order.domain.usecase.GetOrderByIdAndCustomerUseCase;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.repository.query.IOrderQueryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetOrderByIdAndCustomerUseCaseImpl implements GetOrderByIdAndCustomerUseCase {

    private final IOrderQueryRepository orderQueryRepository;

    @Override
    public Order execute(Long orderId, String email) {
        return orderQueryRepository.findByIdAndEmailCustomer(orderId, email)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada"));
    }
}

