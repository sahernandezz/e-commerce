package com.challenge.ecommercebackend.modules.order.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.order.domain.usecase.UpdateOrderStatusUseCase;
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
public class UpdateOrderStatusUseCaseImpl implements UpdateOrderStatusUseCase {

    private final IOrderQueryRepository orderQueryRepository;
    private final IOrderCommandRepository orderCommandRepository;

    @Override
    @Transactional
    public Order execute(Long id, String status) {
        Order order = orderQueryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada"));
        order.setStatus(OrderStatus.valueOf(status));
        order.setUpdatedAt(new Date());
        return orderCommandRepository.save(order);
    }
}

