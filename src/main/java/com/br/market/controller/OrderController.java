package com.br.market.controller;

import com.br.market.dto.ApiResponse;
import com.br.market.dto.OrderRequestDTO;
import com.br.market.dto.OrderResponseDTO;
import com.br.market.exception.InsufficientStockException;
import com.br.market.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        log.info("Recebendo pedido com {} itens", request.getItems().size());

        OrderResponseDTO response = orderService.createOrder(request);

        log.info("Pedido criado com sucesso: id={}", response.getOrderId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<OrderResponseDTO>builder()
                        .success(true)
                        .message("Pedido criado com sucesso")
                        .data(response)
                        .build());
    }
}
