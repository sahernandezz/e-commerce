package com.challenge.ecommercebackend.modules.product.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.persisten.entity.ProductStatus;
import com.challenge.ecommercebackend.modules.product.persisten.repository.command.IProductCommandRepository;
import com.challenge.ecommercebackend.modules.product.persisten.repository.query.IProductQueryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateProductStatusUseCaseImplTest {

    @Mock
    private IProductQueryRepository productQueryRepository;

    @Mock
    private IProductCommandRepository productCommandRepository;

    @InjectMocks
    private UpdateProductStatusUseCaseImpl updateProductStatusUseCase;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L).name("Laptop").price(2000000)
                .status(ProductStatus.ACTIVE).createdAt(new Date())
                .build();
    }

    @Test
    @DisplayName("Debe actualizar el estado de un producto existente")
    void shouldUpdateProductStatus() {
        when(productQueryRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productCommandRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = updateProductStatusUseCase.execute(1L, "INACTIVE");

        assertEquals(ProductStatus.INACTIVE, result.getStatus());
        assertNotNull(result.getUpdatedAt());
        verify(productCommandRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe lanzar EntityNotFoundException cuando producto no existe")
    void shouldThrowWhenProductNotFound() {
        when(productQueryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> updateProductStatusUseCase.execute(99L, "INACTIVE"));
        verify(productCommandRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe lanzar excepción con status inválido")
    void shouldThrowWithInvalidStatus() {
        when(productQueryRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class,
                () -> updateProductStatusUseCase.execute(1L, "INVALID_STATUS"));
    }
}

