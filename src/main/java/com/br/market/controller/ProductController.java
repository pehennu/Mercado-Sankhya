package com.br.market.controller;

import com.br.market.dto.ApiResponse;
import com.br.market.dto.ProductResponseDTO;
import com.br.market.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponseDTO>>> getProducts(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("Buscando produtos: search='{}', page={}, size={}", search, page, size);

        Page<ProductResponseDTO> result = productService.getProducts(search, page, size);

        log.info("Encontrados {} produtos", result.getTotalElements());
        return ResponseEntity.ok(
                ApiResponse.<Page<ProductResponseDTO>>builder()
                        .success(true)
                        .message("Produtos recuperados com sucesso")
                        .data(result)
                        .build()
        );
    }
}
