package com.br.market.service;

import com.br.market.dto.ProductResponseDTO;
import com.br.market.entity.Product;
import com.br.market.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getProducts_withSearch_returnsPagedProducts() {
        Product product1 = Product.builder()
                .id(1L)
                .name("Café Torrado 500g")
                .price(new BigDecimal("18.90"))
                .stock(5)
                .active(true)
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .name("Filtro de Papel nº103")
                .price(new BigDecimal("7.50"))
                .stock(10)
                .active(true)
                .build();

        Page<Product> productPage = new PageImpl<>(List.of(product1, product2), PageRequest.of(0, 10), 2);

        when(productRepository.findByNameContainingIgnoreCase("café", PageRequest.of(0, 10))).thenReturn(productPage);

        Page<ProductResponseDTO> result = productService.getProducts("café", 0, 10);

        assertEquals(2, result.getContent().size());
        assertEquals("Café Torrado 500g", result.getContent().get(0).getName());

        verify(productRepository, times(1)).findByNameContainingIgnoreCase("café", PageRequest.of(0, 10));
    }

    @Test
    void getProducts_withoutSearch_returnsAllProductsPaged() {
        Product product1 = Product.builder()
                .id(1L)
                .name("Café Torrado 500g")
                .price(new BigDecimal("18.90"))
                .stock(5)
                .active(true)
                .build();

        Page<Product> productPage = new PageImpl<>(List.of(product1));

        when(productRepository.findByNameContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(productPage);

        Page<ProductResponseDTO> result = productService.getProducts(null, 0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals("Café Torrado 500g", result.getContent().get(0).getName());

        verify(productRepository, times(1))
                .findByNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }

    @Test
    void getProducts_emptyRepository_returnsEmptyPage() {
        when(productRepository.findByNameContainingIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<ProductResponseDTO> result = productService.getProducts(null, 0, 10);

        assertEquals(0, result.getContent().size());
        verify(productRepository, times(1))
                .findByNameContainingIgnoreCase(anyString(), any(Pageable.class));
    }
}
