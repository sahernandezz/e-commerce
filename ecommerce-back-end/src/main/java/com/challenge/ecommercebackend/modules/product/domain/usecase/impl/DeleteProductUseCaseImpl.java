package com.challenge.ecommercebackend.modules.product.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.product.domain.usecase.DeleteProductUseCase;
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
public class DeleteProductUseCaseImpl implements DeleteProductUseCase {

    private final IProductQueryRepository productQueryRepository;
    private final IProductCommandRepository productCommandRepository;

    @Override
    @Transactional
    public void execute(Long id) {
        Product product = productQueryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        product.setStatus(ProductStatus.INACTIVE);
        product.setUpdatedAt(new Date());
        productCommandRepository.save(product);
    }
}

