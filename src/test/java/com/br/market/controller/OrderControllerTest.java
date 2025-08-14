package com.br.market.controller;

import com.br.market.dto.ApiResponse;
import com.br.market.dto.OrderRequestDTO;
import com.br.market.dto.OrderResponseDTO;
import com.br.market.exception.InsufficientStockException;
import com.br.market.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createOrder_ShouldReturnCreated_WhenSuccessful() {
        OrderRequestDTO request = new OrderRequestDTO();
        OrderResponseDTO responseDTO = OrderResponseDTO.builder()
                .orderId(1L)
                .total(BigDecimal.valueOf(100.0))
                .items(List.of())
                .build();

        when(orderService.createOrder(request)).thenReturn(responseDTO);

        ResponseEntity<ApiResponse<OrderResponseDTO>> response = orderController.createOrder(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1L, Objects.requireNonNull(response.getBody()).getData().getOrderId());
        verify(orderService, times(1)).createOrder(request);
    }
    @Test
    void createOrder_ShouldThrowInsufficientStockException_WhenStockIsLow() {
        OrderRequestDTO request = new OrderRequestDTO();
        InsufficientStockException exception = new InsufficientStockException(List.of());

        when(orderService.createOrder(request)).thenThrow(exception);

        InsufficientStockException thrown = assertThrows(InsufficientStockException.class, () -> orderController.createOrder(request));

        assertNotNull(thrown);
        verify(orderService, times(1)).createOrder(request);
    }
}
