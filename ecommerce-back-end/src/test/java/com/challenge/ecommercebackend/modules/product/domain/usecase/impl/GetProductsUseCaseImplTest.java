package com.challenge.ecommercebackend.modules.product.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import com.challenge.ecommercebackend.modules.product.persisten.entity.CategoryStatus;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.persisten.entity.ProductStatus;
import com.challenge.ecommercebackend.modules.product.persisten.repository.query.IProductQueryRepository;
import com.challenge.ecommercebackend.shared.domain.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetProductsUseCaseImplTest {

    @Mock
    private IProductQueryRepository productQueryRepository;

    @InjectMocks
    private GetProductsUseCaseImpl getProductsUseCase;

    private Product product1;
    private Product product2;
    private Category category;

    @BeforeEach
    void setUp() {
        category = Category.builder()
                .id(1L).name("Electrónica").status(CategoryStatus.ACTIVE).build();

        product1 = Product.builder()
                .id(1L).name("Laptop").price(2000000).status(ProductStatus.ACTIVE)
                .category(category).createdAt(new Date())
                .imagesUrl(List.of("img1.jpg")).colors(List.of("Negro")).sizes(List.of("Único"))
                .build();

        product2 = Product.builder()
                .id(2L).name("Mouse").price(50000).status(ProductStatus.ACTIVE)
                .category(category).createdAt(new Date())
                .imagesUrl(List.of("img2.jpg")).colors(List.of("Blanco")).sizes(List.of("Único"))
                .build();
    }

    @Test
    @DisplayName("Debe retornar todos los productos activos")
    void shouldReturnAllActiveProducts() {
        when(productQueryRepository.findAllActive()).thenReturn(List.of(product1, product2));

        List<Product> result = getProductsUseCase.getAllActive();

        assertEquals(2, result.size());
        verify(productQueryRepository).findAllActive();
    }

    @Test
    @DisplayName("Debe retornar productos activos por categoría")
    void shouldReturnActiveProductsByCategoryId() {
        when(productQueryRepository.findAllByCategoryId(1L)).thenReturn(List.of(product1, product2));

        List<Product> result = getProductsUseCase.getActiveByCategoryId(1L);

        assertEquals(2, result.size());
        verify(productQueryRepository).findAllByCategoryId(1L);
    }

    @Test
    @DisplayName("Debe retornar productos activos por nombre")
    void shouldReturnActiveProductsByName() {
        when(productQueryRepository.findAllByNameContainingIgnoreCase("Laptop")).thenReturn(List.of(product1));

        List<Product> result = getProductsUseCase.getActiveByName("Laptop");

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
    }

    @Test
    @DisplayName("Debe retornar un producto por ID")
    void shouldReturnProductById() {
        when(productQueryRepository.findActiveById(1L)).thenReturn(Optional.of(product1));

        Product result = getProductsUseCase.getById(1L);

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
    }

    @Test
    @DisplayName("Debe lanzar EntityNotFoundException cuando el producto no existe")
    void shouldThrowWhenProductNotFound() {
        when(productQueryRepository.findActiveById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> getProductsUseCase.getById(99L));
    }

    @Test
    @DisplayName("Debe retornar productos paginados sin filtro")
    void shouldReturnPaginatedWithoutFilters() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> page = new PageImpl<>(List.of(product1, product2), pageable, 2);
        when(productQueryRepository.findAll(any(Pageable.class))).thenReturn(page);

        PageResponse<Product> result = getProductsUseCase.getAllPaginated(0, 10, null, null);

        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPage());
        assertEquals(1, result.getTotalPages());
    }

    @Test
    @DisplayName("Debe filtrar por status cuando se proporciona")
    void shouldFilterByStatus() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> page = new PageImpl<>(List.of(product1), pageable, 1);
        when(productQueryRepository.findByStatus(eq(ProductStatus.ACTIVE), any(Pageable.class))).thenReturn(page);

        PageResponse<Product> result = getProductsUseCase.getAllPaginated(0, 10, "ACTIVE", null);

        assertEquals(1, result.getContent().size());
    }

    @Test
    @DisplayName("Debe filtrar por búsqueda cuando se proporciona")
    void shouldFilterBySearch() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> page = new PageImpl<>(List.of(product1), pageable, 1);
        when(productQueryRepository.findByNameContainingIgnoreCase(eq("Laptop"), any(Pageable.class))).thenReturn(page);

        PageResponse<Product> result = getProductsUseCase.getAllPaginated(0, 10, null, "Laptop");

        assertEquals(1, result.getContent().size());
    }

    @Test
    @DisplayName("Debe filtrar por status y búsqueda")
    void shouldFilterByStatusAndSearch() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> page = new PageImpl<>(List.of(product1), pageable, 1);
        when(productQueryRepository.findByStatusAndNameContainingIgnoreCase(
                eq(ProductStatus.ACTIVE), eq("Laptop"), any(Pageable.class))).thenReturn(page);

        PageResponse<Product> result = getProductsUseCase.getAllPaginated(0, 10, "ACTIVE", "Laptop");

        assertEquals(1, result.getContent().size());
    }

    @Test
    @DisplayName("Debe retornar lista vacía cuando no hay productos")
    void shouldReturnEmptyWhenNoProducts() {
        when(productQueryRepository.findAllActive()).thenReturn(List.of());

        List<Product> result = getProductsUseCase.getAllActive();

        assertTrue(result.isEmpty());
    }
}

