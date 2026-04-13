package com.challenge.ecommercebackend.modules.product.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import com.challenge.ecommercebackend.modules.product.persisten.entity.CategoryStatus;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.persisten.entity.ProductStatus;
import com.challenge.ecommercebackend.modules.product.persisten.repository.command.IProductCommandRepository;
import com.challenge.ecommercebackend.modules.product.persisten.repository.query.ICategoryQueryRepository;
import com.challenge.ecommercebackend.modules.product.web.dto.request.InputProductRequest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseImplTest {

    @Mock
    private IProductCommandRepository productCommandRepository;

    @Mock
    private ICategoryQueryRepository categoryQueryRepository;

    @InjectMocks
    private CreateProductUseCaseImpl createProductUseCase;

    private Category category;
    private InputProductRequest request;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L)
                .name("Electrónica")
                .status(CategoryStatus.ACTIVE)
                .build();

        request = new InputProductRequest(
                "Laptop Gaming",
                "Laptop de alta gama",
                2500000,
                1L,
                10,
                List.of("https://img.com/laptop.jpg"),
                List.of("Negro", "Gris"),
                List.of("Único")
        );
    }

    @Test
    @DisplayName("Debe crear un producto correctamente cuando la categoría existe")
    void shouldCreateProductWhenCategoryExists() {
        when(categoryQueryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productCommandRepository.save(any(Product.class))).thenAnswer(inv -> {
            Product p = inv.getArgument(0);
            p.setId(1L);
            return p;
        });

        Product result = createProductUseCase.execute(request);

        assertNotNull(result);
        assertEquals("Laptop Gaming", result.getName());
        assertEquals(2500000, result.getPrice());
        assertEquals(ProductStatus.ACTIVE, result.getStatus());
        assertEquals(category, result.getCategory());
        assertNotNull(result.getCreatedAt());

        verify(categoryQueryRepository).findById(1L);
        verify(productCommandRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("Debe lanzar EntityNotFoundException cuando la categoría no existe")
    void shouldThrowWhenCategoryNotFound() {
        when(categoryQueryRepository.findById(99L)).thenReturn(Optional.empty());

        InputProductRequest badRequest = new InputProductRequest(
                "Producto", "Desc", 1000, 99L, 0,
                List.of("img.jpg"), List.of(), List.of()
        );

        assertThrows(EntityNotFoundException.class, () -> createProductUseCase.execute(badRequest));
        verify(productCommandRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe asignar estado ACTIVE al producto creado")
    void shouldSetActiveStatusOnCreatedProduct() {
        when(categoryQueryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productCommandRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product result = createProductUseCase.execute(request);

        assertEquals(ProductStatus.ACTIVE, result.getStatus());
    }
}

