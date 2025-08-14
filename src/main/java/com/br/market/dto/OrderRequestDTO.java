package com.br.market.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDTO {

    @NotEmpty
    @Valid
    private List<OrderItemRequest> items = new ArrayList<>();

    @Getter @AllArgsConstructor @Builder
    public static class OrderItemRequest {
        @NotNull
        private Long productId;

        @Min(1)
        private Integer quantity;
    }
}
