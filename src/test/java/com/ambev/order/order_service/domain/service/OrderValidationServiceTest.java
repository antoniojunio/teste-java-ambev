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

@DisplayName("OrderValidationService Tests")
class OrderValidationServiceTest {

    private OrderValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new OrderValidationService();
    }

    @Test
    @DisplayName("Deve validar pedido válido com sucesso")
    void deveValidarPedidoValido() {
        Order order = createValidOrder();
        assertDoesNotThrow(() -> validationService.validate(order));
    }

    @Test
    @DisplayName("Deve lançar exceção quando pedido é nulo")
    void deveLancarExcecaoQuandoPedidoNulo() {
        assertThrows(IllegalArgumentException.class, () -> validationService.validate(null));
    }

    @Test
    @DisplayName("Deve lançar exceção quando externalId é nulo")
    void deveLancarExcecaoQuandoExternalIdNulo() {
        Order order = createValidOrder();
        order.setExternalId(null);
        assertThrows(IllegalArgumentException.class, () -> validationService.validate(order));
    }

    @Test
    @DisplayName("Deve lançar exceção quando lista de produtos está vazia")
    void deveLancarExcecaoQuandoListaProdutosVazia() {
        Order order = createValidOrder();
        order.setProducts(new ArrayList<>());
        assertThrows(IllegalArgumentException.class, () -> validationService.validate(order));
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto tem valor inválido")
    void deveLancarExcecaoQuandoProdutoValorInvalido() {
        Order order = createValidOrder();
        order.getProducts().get(0).setValue(BigDecimal.ZERO);
        assertThrows(IllegalArgumentException.class, () -> validationService.validate(order));
    }

    private Order createValidOrder() {
        List<Product> products = new ArrayList<>();
        products.add(Product.builder()
                .name("Produto A")
                .value(new BigDecimal("10.50"))
                .quantity(2)
                .build());
        
        return Order.builder()
                .externalId("EXT-123")
                .products(products)
                .build();
    }
}

