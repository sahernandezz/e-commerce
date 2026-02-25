package com.challenge.ecommercebackend.modules.product.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.product.domain.usecase.UpdateProductStatusUseCase;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.persisten.entity.ProductStatus;
import com.challenge.ecommercebackend.modules.product.persisten.repository.command.IProductCommandRepository;
import com.challenge.ecommercebackend.modules.product.persisten.repository.query.IProductQueryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UpdateProductStatusUseCaseImpl implements UpdateProductStatusUseCase {

    private final IProductQueryRepository productQueryRepository;
    private final IProductCommandRepository productCommandRepository;

    @Override
    @Transactional
    public Product execute(Long id, String status) {
        Product product = productQueryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        product.setStatus(ProductStatus.valueOf(status));
        product.setUpdatedAt(new Date());

        return productCommandRepository.save(product);
    }
}
