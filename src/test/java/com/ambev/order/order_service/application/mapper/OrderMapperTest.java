package com.ambev.order.order_service.application.mapper;

import com.ambev.order.order_service.application.dto.OrderRequestDTO;
import com.ambev.order.order_service.application.dto.OrderResponseDTO;
import com.ambev.order.order_service.application.dto.ProductDTO;
import com.ambev.order.order_service.domain.model.Order;
import com.ambev.order.order_service.domain.model.OrderStatus;
import com.ambev.order.order_service.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@DisplayName("OrderMapper Tests")
class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    @DisplayName("Deve mapear OrderRequestDTO para Order")
    void deveMapearOrderRequestDTOParaOrder() {
        OrderRequestDTO dto = createOrderRequestDTO();
        Order order = orderMapper.toEntityWithProducts(dto);

        assertNotNull(order);
        assertEquals(dto.getExternalId(), order.getExternalId());
        assertNotNull(order.getProducts());
        assertEquals(dto.getProducts().size(), order.getProducts().size());
    }

    @Test
    @DisplayName("Deve mapear Order para OrderResponseDTO")
    void deveMapearOrderParaOrderResponseDTO() {
        Order order = createOrder();
        OrderResponseDTO dto = orderMapper.toDTO(order);

        assertNotNull(dto);
        assertEquals(order.getId(), dto.getId());
        assertEquals(order.getExternalId(), dto.getExternalId());
        assertEquals(order.getTotalValue(), dto.getTotalValue());
        assertEquals(order.getStatus(), dto.getStatus());
    }

    private OrderRequestDTO createOrderRequestDTO() {
        List<ProductDTO> products = new ArrayList<>();
        products.add(ProductDTO.builder()
                .name("Produto A")
                .value(new BigDecimal("10.50"))
                .quantity(2)
                .build());

        return OrderRequestDTO.builder()
                .externalId("EXT-123")
                .products(products)
                .build();
    }

    private Order createOrder() {
        List<Product> products = new ArrayList<>();
        products.add(Product.builder()
                .id(1L)
                .name("Produto A")
                .value(new BigDecimal("10.50"))
                .quantity(2)
                .build());

        return Order.builder()
                .id(1L)
                .externalId("EXT-123")
                .totalValue(new BigDecimal("21.00"))
                .status(OrderStatus.PROCESSED)
                .createdAt(LocalDateTime.now())
                .products(products)
                .build();
    }
}

