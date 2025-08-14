package com.br.market.service;

import com.br.market.dto.ProductResponseDTO;
import com.br.market.entity.Product;
import com.br.market.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductResponseDTO> getProducts(String search, int page, int size) {
        log.info("Buscando produtos com search='{}', page={}, size={}", search, page, size);

        if (page < 0 || size <= 0) {
            log.warn("Parâmetros de paginação inválidos: page={}, size={}", page, size);
            throw new IllegalArgumentException("Parâmetros de paginação inválidos");
        }

        String query = (search == null) ? "" : search.trim();
        if (query.length() > 120) {
            query = query.substring(0, 120);
            log.debug("Query truncada para 120 caracteres: '{}'", query);
        }

        Page<ProductResponseDTO> result = productRepository
                .findByNameContainingIgnoreCase(query, PageRequest.of(page, size))
                .map(this::toDTO);

        log.info("Retornando {} produtos", result.getContent().size());
        return result;
    }

    private ProductResponseDTO toDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .active(product.getActive())
                .build();
    }
}
