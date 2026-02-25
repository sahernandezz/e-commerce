package com.challenge.ecommercebackend.modules.order.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.order.domain.usecase.CancelOrderByCustomerUseCase;
import com.challenge.ecommercebackend.modules.order.domain.usecase.GetOrderByIdAndCustomerUseCase;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import com.challenge.ecommercebackend.modules.order.persisten.repository.command.IOrderCommandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CancelOrderByCustomerUseCaseImpl implements CancelOrderByCustomerUseCase {

    private final IOrderCommandRepository orderCommandRepository;
    private final GetOrderByIdAndCustomerUseCase getOrderByIdAndCustomerUseCase;

    @Override
    @Transactional
    public void execute(Long orderId, String email) {
        Order order = getOrderByIdAndCustomerUseCase.execute(orderId, email);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden cancelar ordenes pendientes");
        }
        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(new Date());
        orderCommandRepository.save(order);
    }
}

