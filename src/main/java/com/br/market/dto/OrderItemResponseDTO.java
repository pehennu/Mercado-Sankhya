package com.br.market.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponseDTO {
    private Long productId;
    private String name;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}
