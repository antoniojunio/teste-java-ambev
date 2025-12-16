package com.ambev.order.order_service.infrastructure.persistence;

import com.ambev.order.order_service.domain.model.Order;
import com.ambev.order.order_service.domain.model.OrderStatus;
import com.ambev.order.order_service.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("OrderRepository Integration Tests")
class OrderRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve salvar e buscar pedido por ID")
    void deveSalvarEBuscarPedidoPorId() {
        Order order = createOrder();
        Order saved = orderRepository.save(order);

        Optional<Order> found = orderRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getExternalId(), found.get().getExternalId());
    }

    @Test
    @DisplayName("Deve buscar pedido por externalId")
    void deveBuscarPedidoPorExternalId() {
        Order order = createOrder();
        orderRepository.save(order);

        Optional<Order> found = orderRepository.findByExternalId("EXT-123");

        assertTrue(found.isPresent());
        assertEquals("EXT-123", found.get().getExternalId());
    }

    @Test
    @DisplayName("Deve verificar se pedido existe por externalId")
    void deveVerificarSePedidoExistePorExternalId() {
        Order order = createOrder();
        orderRepository.save(order);

        boolean exists = orderRepository.existsByExternalId("EXT-123");

        assertTrue(exists);
    }

    private Order createOrder() {
        List<Product> products = new ArrayList<>();
        products.add(Product.builder()
                .name("Produto A")
                .value(new BigDecimal("10.50"))
                .quantity(2)
                .build());

        return Order.builder()
                .externalId("EXT-123")
                .totalValue(new BigDecimal("21.00"))
                .status(OrderStatus.PROCESSED)
                .products(products)
                .build();
    }
}

