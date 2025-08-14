package com.br.market.exception;

import com.br.market.dto.StockErrorDTO;
import lombok.*;

import java.util.List;

@Getter
public class InsufficientStockException extends RuntimeException {
    private final List<StockErrorDTO> items;

    public InsufficientStockException(List<StockErrorDTO> items) {
        super("Produtos insuficientes no estoque.");
        this.items = items;
    }
}
