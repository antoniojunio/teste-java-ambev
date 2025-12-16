package com.ambev.order.order_service.infrastructure.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@DisplayName("RedisDuplicateService Integration Tests")
class RedisDuplicateServiceTest {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", redis::getFirstMappedPort);
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:testdb");
        registry.add("spring.rabbitmq.host", () -> "localhost");
        registry.add("spring.rabbitmq.port", () -> 5672);
    }

    @Autowired
    private RedisDuplicateService duplicateService;

    @BeforeEach
    void setUp() {
        duplicateService.remove("EXT-123");
    }

    @Test
    @DisplayName("Deve verificar que pedido não é duplicado inicialmente")
    void deveVerificarQuePedidoNaoEDuplicadoInicialmente() {
        boolean isDuplicate = duplicateService.isDuplicate("EXT-123");
        assertFalse(isDuplicate);
    }

    @Test
    @DisplayName("Deve marcar pedido como processado e verificar duplicação")
    void deveMarcarPedidoComoProcessadoEVerificarDuplicacao() {
        duplicateService.markAsProcessed("EXT-123");
        
        boolean isDuplicate = duplicateService.isDuplicate("EXT-123");
        assertTrue(isDuplicate);
    }

    @Test
    @DisplayName("Deve remover pedido do cache")
    void deveRemoverPedidoDoCache() {
        duplicateService.markAsProcessed("EXT-123");
        duplicateService.remove("EXT-123");
        
        boolean isDuplicate = duplicateService.isDuplicate("EXT-123");
        assertFalse(isDuplicate);
    }
}

