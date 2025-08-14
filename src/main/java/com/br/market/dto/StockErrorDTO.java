package com.br.market.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockErrorDTO {
    private Long productId;
    private Integer available;
}
