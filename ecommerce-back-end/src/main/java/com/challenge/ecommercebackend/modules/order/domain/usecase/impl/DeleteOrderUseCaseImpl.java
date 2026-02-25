package com.challenge.ecommercebackend.modules.order.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.order.domain.usecase.DeleteOrderUseCase;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import com.challenge.ecommercebackend.modules.order.persisten.repository.command.IOrderCommandRepository;
import com.challenge.ecommercebackend.modules.order.persisten.repository.query.IOrderQueryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class DeleteOrderUseCaseImpl implements DeleteOrderUseCase {

    private final IOrderQueryRepository orderQueryRepository;
    private final IOrderCommandRepository orderCommandRepository;

    @Override
    @Transactional
    public void execute(Long orderId) {
        Order order = orderQueryRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada"));
        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(new Date());
        orderCommandRepository.save(order);
    }
}

