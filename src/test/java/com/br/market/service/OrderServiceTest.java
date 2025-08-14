package com.br.market.service;

import com.br.market.dto.OrderRequestDTO;
import com.br.market.dto.OrderResponseDTO;
import com.br.market.entity.Order;
import com.br.market.entity.Product;
import com.br.market.exception.InsufficientStockException;
import com.br.market.repository.OrderRepository;
import com.br.market.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Product product1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product1 = Product.builder()
                .id(1L)
                .name("Café Torrado")
                .price(new BigDecimal("18.90"))
                .stock(5)
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("Filtro de Papel")
                .price(new BigDecimal("7.50"))
                .stock(0)
                .build();
    }

    @Test
    void createOrder_success() {
        OrderRequestDTO request = new OrderRequestDTO(List.of(
                new OrderRequestDTO.OrderItemRequest(1L, 2)
        ));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderResponseDTO createdOrder = orderService.createOrder(request);

        assertNotNull(createdOrder);
        assertEquals(2, createdOrder.getItems().get(0).getQuantity());
        assertEquals(new BigDecimal("37.80"), createdOrder.getItems().get(0).getLineTotal());

        assertEquals(3, product1.getStock());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    void createOrder_insufficientStock() {
        Product product = Product.builder()
                .id(1L)
                .name("Produto Teste")
                .price(BigDecimal.TEN)
                .stock(1) // estoque insuficiente
                .active(true)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        OrderRequestDTO request = new OrderRequestDTO(List.of(
                new OrderRequestDTO.OrderItemRequest(1L, 2)
        ));

        InsufficientStockException exception = assertThrows(
                InsufficientStockException.class,
                () -> orderService.createOrder(request)
        );

        assertTrue(exception.getMessage().contains("Produtos insuficientes no estoque."));
    }
}
