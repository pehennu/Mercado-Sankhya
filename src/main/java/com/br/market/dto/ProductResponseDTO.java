package com.br.market.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private Boolean active;
}
