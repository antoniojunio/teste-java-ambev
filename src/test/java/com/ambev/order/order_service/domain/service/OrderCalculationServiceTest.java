package com.ambev.order.order_service.domain.service;

import com.ambev.order.order_service.domain.model.Order;
import com.ambev.order.order_service.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("OrderCalculationService Tests")
class OrderCalculationServiceTest {

    private OrderCalculationService calculationService;

    @BeforeEach
    void setUp() {
        calculationService = new OrderCalculationService();
    }

    @Test
    @DisplayName("Deve calcular total corretamente com múltiplos produtos")
    void deveCalcularTotalComMultiplosProdutos() {
        Order order = Order.builder()
                .products(createProducts())
                .build();

        BigDecimal total = calculationService.calculateTotal(order);

        assertEquals(new BigDecimal("35.50"), total);
    }

    @Test
    @DisplayName("Deve retornar zero quando pedido é nulo")
    void deveRetornarZeroQuandoPedidoNulo() {
        BigDecimal total = calculationService.calculateTotal(null);
        assertEquals(BigDecimal.ZERO, total);
    }

    @Test
    @DisplayName("Deve retornar zero quando lista de produtos está vazia")
    void deveRetornarZeroQuandoListaVazia() {
        Order order = Order.builder()
                .products(new ArrayList<>())
                .build();

        BigDecimal total = calculationService.calculateTotal(order);
        assertEquals(BigDecimal.ZERO, total);
    }

    private List<Product> createProducts() {
        List<Product> products = new ArrayList<>();
        products.add(Product.builder()
                .name("Produto A")
                .value(new BigDecimal("10.50"))
                .quantity(2)
                .build());
        products.add(Product.builder()
                .name("Produto B")
                .value(new BigDecimal("14.50"))
                .quantity(1)
                .build());
        return products;
    }
}

