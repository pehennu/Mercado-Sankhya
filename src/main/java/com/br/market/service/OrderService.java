package com.br.market.service;

import com.br.market.dto.OrderItemResponseDTO;
import com.br.market.dto.OrderRequestDTO;
import com.br.market.dto.OrderResponseDTO;
import com.br.market.dto.StockErrorDTO;
import com.br.market.entity.Order;
import com.br.market.entity.OrderItem;
import com.br.market.entity.Product;
import com.br.market.exception.InsufficientStockException;
import com.br.market.repository.OrderRepository;
import com.br.market.repository.ProductRepository;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        log.info("Iniciando criação de pedido com {} itens", request.getItems().size());

        List<StockErrorDTO> outOfStock = new ArrayList<>();
        Map<Long, Product> productsMap = new HashMap<>();

        for (OrderRequestDTO.OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> {
                        log.error("Produto não encontrado: {}", itemReq.getProductId());
                        return new IllegalArgumentException("Esse produto não foi encontrado: " + itemReq.getProductId());
                    });

            if (itemReq.getQuantity() > product.getStock()) {
                log.warn("Estoque insuficiente para produto {}: solicitado={}, disponível={}",
                        product.getName(), itemReq.getQuantity(), product.getStock());
                outOfStock.add(StockErrorDTO.builder()
                        .productId(product.getId())
                        .available(product.getStock())
                        .build());
            }

            productsMap.put(product.getId(), product);
        }

        if (!outOfStock.isEmpty()) {
            log.warn("Não foi possível criar pedido, produtos sem estoque suficiente: {}", outOfStock);
            throw new InsufficientStockException(outOfStock);
        }

        Order order = Order.builder()
                .createdAt(LocalDateTime.now())
                .total(BigDecimal.ZERO)
                .items(new ArrayList<>())
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderRequestDTO.OrderItemRequest itemReq : request.getItems()) {
            Product product = productsMap.get(itemReq.getProductId());

            BigDecimal lineTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemReq.getQuantity()))
                    .setScale(2, RoundingMode.HALF_EVEN);

            total = total.add(lineTotal);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(product.getPrice())
                    .lineTotal(lineTotal)
                    .build();

            order.getItems().add(orderItem);

            product.setStock(product.getStock() - itemReq.getQuantity());
            productRepository.save(product);
            log.debug("Estoque atualizado para produto {}: novo estoque={}", product.getName(), product.getStock());
        }

        order.setTotal(total.setScale(2, RoundingMode.HALF_EVEN));

        try {
            orderRepository.save(order);
            log.info("Pedido criado com sucesso: id={}", order.getId());
        } catch (OptimisticLockException e) {
            log.error("Conflito de estoque ao salvar pedido: {}", e.getMessage());
            throw new RuntimeException("Ocorreu um conflito no estoque, tente novamente.");
        }

        List<OrderItemResponseDTO> itemsDTO = order.getItems().stream()
                .map(i -> OrderItemResponseDTO.builder()
                        .productId(i.getProduct().getId())
                        .name(i.getProduct().getName())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .lineTotal(i.getLineTotal())
                        .build())
                .toList();

        return OrderResponseDTO.builder()
                .orderId(order.getId())
                .createdAt(order.getCreatedAt())
                .total(order.getTotal())
                .items(itemsDTO)
                .build();
    }
}
