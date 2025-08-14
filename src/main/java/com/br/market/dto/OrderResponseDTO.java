package com.br.market.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long orderId;
    private LocalDateTime createdAt;
    private BigDecimal total;
    private List<OrderItemResponseDTO> items;
}
