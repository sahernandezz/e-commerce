package com.challenge.ecommercebackend.modules.order.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.order.domain.usecase.GetOrdersByCustomerUseCase;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.repository.query.IOrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetOrdersByCustomerUseCaseImpl implements GetOrdersByCustomerUseCase {

    private final IOrderQueryRepository orderQueryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Order> execute(String email, Pageable pageable) {
        return orderQueryRepository.findByEmailCustomer(email, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> execute(String email) {
        return orderQueryRepository.findByEmailCustomerOrderByCreatedAtDesc(email);
    }
}

