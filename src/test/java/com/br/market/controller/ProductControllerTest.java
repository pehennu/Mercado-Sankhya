package com.br.market.controller;

import com.br.market.dto.ApiResponse;
import com.br.market.dto.ProductResponseDTO;
import com.br.market.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProducts_returnsPagedProducts() {
        ProductResponseDTO product1 = ProductResponseDTO.builder()
                .id(1L)
                .name("Café Torrado 500g")
                .price(new BigDecimal("18.90"))
                .stock(5)
                .active(true)
                .build();

        ProductResponseDTO product2 = ProductResponseDTO.builder()
                .id(2L)
                .name("Filtro de Papel nº103")
                .price(new BigDecimal("7.50"))
                .stock(10)
                .active(true)
                .build();

        Page<ProductResponseDTO> page = new PageImpl<>(List.of(product1, product2), PageRequest.of(0, 10), 2);

        when(productService.getProducts("café", 0, 10)).thenReturn(page);

        ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> response = productController.getProducts("café", 0, 10);

        assertEquals(2, Objects.requireNonNull(response.getBody()).getData().getContent().size());
        assertEquals("Café Torrado 500g", response.getBody().getData().getContent().get(0).getName());

        verify(productService, times(1)).getProducts("café", 0, 10);
    }

    @Test
    void getProducts_emptyList() {
        Page<ProductResponseDTO> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);
        when(productService.getProducts(null, 0, 10)).thenReturn(emptyPage);

        ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> response = productController.getProducts(null, 0, 10);

        assertEquals(0, Objects.requireNonNull(response.getBody()).getData().getContent().size());
        verify(productService, times(1)).getProducts(null, 0, 10);
    }
}
