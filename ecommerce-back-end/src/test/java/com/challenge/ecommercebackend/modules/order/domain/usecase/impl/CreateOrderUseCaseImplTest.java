package com.challenge.ecommercebackend.modules.order.domain.usecase.impl;

import com.challenge.ecommercebackend.api.IProductExternalApi;
import com.challenge.ecommercebackend.email.IEmailSenderService;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import com.challenge.ecommercebackend.modules.order.persisten.repository.command.IOrderCommandRepository;
import com.challenge.ecommercebackend.modules.order.web.dto.request.InputOrderProductRequest;
import com.challenge.ecommercebackend.modules.order.web.dto.request.InputOrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseImplTest {

    @Mock
    private IOrderCommandRepository orderCommandRepository;

    @Mock
    private IProductExternalApi productExternalApi;

    @Mock
    private IEmailSenderService emailSenderService;

    @InjectMocks
    private CreateOrderUseCaseImpl createOrderUseCase;

    private InputOrderRequest validRequest;

    @BeforeEach
    void setUp() {
        InputOrderProductRequest productRequest = new InputOrderProductRequest(2, 1L, "Negro", "M");
        validRequest = new InputOrderRequest(
                "test@email.com", "Calle 123", "Medellín",
                "Descripción", "CASH", List.of(productRequest)
        );
    }

    @Test
    @DisplayName("Debe crear una orden correctamente")
    void shouldCreateOrderSuccessfully() {
        Map<String, Object> productData = Map.of("name", "Laptop", "price", 2000000, "discount", 0);
        when(productExternalApi.getProductById(1L)).thenReturn(productData);
        when(orderCommandRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(1L);
            return o;
        });
        doNothing().when(emailSenderService).sendEmail(anyString(), anyString(), anyString());

        String result = createOrderUseCase.execute(validRequest);

        assertEquals("Order created successfully", result);
        verify(orderCommandRepository, times(2)).save(any(Order.class));
        verify(emailSenderService).sendEmail(eq("test@email.com"), anyString(), anyString());
    }

    @Test
    @DisplayName("Debe retornar error cuando falla la creación")
    void shouldReturnErrorWhenCreationFails() {
        when(productExternalApi.getProductById(1L)).thenThrow(new RuntimeException("Product not found"));

        String result = createOrderUseCase.execute(validRequest);

        assertEquals("Error creating order", result);
    }

    @Test
    @DisplayName("Debe calcular el total correctamente")
    void shouldCalculateTotalCorrectly() {
        Map<String, Object> productData = Map.of("name", "Mouse", "price", 50000, "discount", 0);
        when(productExternalApi.getProductById(1L)).thenReturn(productData);
        when(orderCommandRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(1L);
            // Verify total = price * quantity = 50000 * 2 = 100000
            assertEquals(100000, o.getTotal());
            assertEquals(OrderStatus.PENDING, o.getStatus());
            return o;
        });
        doNothing().when(emailSenderService).sendEmail(anyString(), anyString(), anyString());

        createOrderUseCase.execute(validRequest);

        verify(orderCommandRepository, times(2)).save(any(Order.class));
    }

    @Test
    @DisplayName("Debe convertir email a minúsculas y trim")
    void shouldNormalizeEmail() {
        InputOrderProductRequest productRequest = new InputOrderProductRequest(1, 1L, "Rojo", "L");
        InputOrderRequest request = new InputOrderRequest(
                "  TEST@Email.COM  ", "Calle 1", "Bogotá",
                null, "CREDIT_CARD", List.of(productRequest)
        );

        Map<String, Object> productData = Map.of("name", "Shirt", "price", 80000, "discount", 0);
        when(productExternalApi.getProductById(1L)).thenReturn(productData);
        when(orderCommandRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(1L);
            assertEquals("test@email.com", o.getEmailCustomer());
            return o;
        });
        doNothing().when(emailSenderService).sendEmail(anyString(), anyString(), anyString());

        createOrderUseCase.execute(request);
    }
}

