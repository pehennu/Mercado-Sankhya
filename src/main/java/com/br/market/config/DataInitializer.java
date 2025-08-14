package com.br.market.config;

import com.br.market.entity.Order;
import com.br.market.entity.OrderItem;
import com.br.market.entity.Product;
import com.br.market.repository.OrderRepository;
import com.br.market.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            List<Product> products = List.of(
                    Product.builder().name("Café Torrado 500g").price(new BigDecimal("18.90")).stock(5).active(true).build(),
                    Product.builder().name("Filtro de Papel nº103").price(new BigDecimal("7.50")).stock(10).active(true).build(),
                    Product.builder().name("Garrafa Térmica 1L").price(new BigDecimal("79.90")).stock(2).active(true).build(),
                    Product.builder().name("Açúcar Mascavo 1kg").price(new BigDecimal("16.00")).stock(0).active(true).build(),
                    Product.builder().name("Caneca Inox 300ml").price(new BigDecimal("29.00")).stock(8).active(true).build()
            );

            productRepository.saveAll(products);
            System.out.println("Massa inicial de produtos cadastradas.");

            Product cafe = products.get(0);
            Product filtro = products.get(1);
            Product garrafa = products.get(2);

            // Pedido 1
            Order order1 = Order.builder()
                    .createdAt(LocalDateTime.now())
                    .items(List.of(
                            OrderItem.builder()
                                    .product(cafe)
                                    .quantity(2)
                                    .unitPrice(cafe.getPrice())
                                    .lineTotal(cafe.getPrice().multiply(BigDecimal.valueOf(2)))
                                    .build(),
                            OrderItem.builder()
                                    .product(filtro)
                                    .quantity(1)
                                    .unitPrice(filtro.getPrice())
                                    .lineTotal(filtro.getPrice())
                                    .build()
                    ))
                    .total(cafe.getPrice().multiply(BigDecimal.valueOf(2)).add(filtro.getPrice()))
                    .build();

            Order order2 = Order.builder()
                    .createdAt(LocalDateTime.now())
                    .items(List.of(
                            OrderItem.builder()
                                    .product(garrafa)
                                    .quantity(1)
                                    .unitPrice(garrafa.getPrice())
                                    .lineTotal(garrafa.getPrice())
                                    .build(),
                            OrderItem.builder()
                                    .product(cafe)
                                    .quantity(1)
                                    .unitPrice(cafe.getPrice())
                                    .lineTotal(cafe.getPrice())
                                    .build()
                    ))
                    .total(garrafa.getPrice().add(cafe.getPrice()))
                    .build();

            order1.getItems().forEach(item -> item.setOrder(order1));
            order2.getItems().forEach(item -> item.setOrder(order2));

            orderRepository.saveAll(List.of(order1, order2));

            System.out.println("Pedidos de exemplo cadastrados.");
        }
    }
}
