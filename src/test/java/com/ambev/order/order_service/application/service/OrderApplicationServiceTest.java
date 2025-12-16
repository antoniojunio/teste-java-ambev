package com.ambev.order.order_service.application.service;

import com.ambev.order.order_service.application.dto.OrderRequestDTO;
import com.ambev.order.order_service.application.dto.OrderResponseDTO;
import com.ambev.order.order_service.application.dto.ProductDTO;
import com.ambev.order.order_service.application.mapper.OrderMapper;
import com.ambev.order.order_service.domain.model.Order;
import com.ambev.order.order_service.domain.model.OrderStatus;
import com.ambev.order.order_service.domain.model.Product;
import com.ambev.order.order_service.domain.repository.IOrderRepository;
import com.ambev.order.order_service.domain.service.OrderCalculationService;
import com.ambev.order.order_service.domain.service.OrderValidationService;
import com.ambev.order.order_service.exception.DuplicateOrderException;
import com.ambev.order.order_service.infrastructure.cache.DistributedLockService;
import com.ambev.order.order_service.infrastructure.cache.RedisDuplicateService;
import com.ambev.order.order_service.infrastructure.messaging.OrderEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderApplicationService Tests")
class OrderApplicationServiceTest {

    @Mock
    private IOrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderCalculationService calculationService;

    @Mock
    private OrderValidationService validationService;

    @Mock
    private RedisDuplicateService duplicateService;

    @Mock
    private DistributedLockService lockService;

    @Mock
    private OrderEventPublisher eventPublisher;

    @InjectMocks
    private OrderApplicationService applicationService;

    private OrderRequestDTO requestDTO;
    private Order order;
    private OrderResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = createOrderRequestDTO();
        order = createOrder();
        responseDTO = createOrderResponseDTO();
    }

    @Test
    @DisplayName("Deve processar pedido com sucesso")
    void deveProcessarPedidoComSucesso() {
        when(lockService.tryLock(anyString())).thenReturn(true);
        when(duplicateService.isDuplicate(anyString())).thenReturn(false);
        when(orderRepository.existsByExternalId(anyString())).thenReturn(false);
        when(orderMapper.toEntity(any(OrderRequestDTO.class))).thenReturn(order);
        when(calculationService.calculateTotal(any(Order.class))).thenReturn(new BigDecimal("21.00"));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDTO(any(Order.class))).thenReturn(responseDTO);

        OrderResponseDTO result = applicationService.processOrder(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        verify(lockService).tryLock(anyString());
        verify(duplicateService).isDuplicate(anyString());
        verify(orderRepository).save(any(Order.class));
        verify(eventPublisher).publishOrderProcessed(any(OrderResponseDTO.class));
        verify(lockService).releaseLock(anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando lock não é adquirido")
    void deveLancarExcecaoQuandoLockNaoAdquirido() {
        when(lockService.tryLock(anyString())).thenReturn(false);

        assertThrows(DuplicateOrderException.class, () -> applicationService.processOrder(requestDTO));
        verify(lockService).releaseLock(anyString());
    }

    @Test
    @DisplayName("Deve lançar exceção quando pedido é duplicado")
    void deveLancarExcecaoQuandoPedidoDuplicado() {
        when(lockService.tryLock(anyString())).thenReturn(true);
        when(duplicateService.isDuplicate(anyString())).thenReturn(true);

        assertThrows(DuplicateOrderException.class, () -> applicationService.processOrder(requestDTO));
        verify(lockService).releaseLock(anyString());
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
                .name("Produto A")
                .value(new BigDecimal("10.50"))
                .quantity(2)
                .build());

        return Order.builder()
                .id(1L)
                .externalId("EXT-123")
                .totalValue(new BigDecimal("21.00"))
                .status(OrderStatus.PROCESSED)
                .products(products)
                .build();
    }

    private OrderResponseDTO createOrderResponseDTO() {
        return OrderResponseDTO.builder()
                .id(1L)
                .externalId("EXT-123")
                .totalValue(new BigDecimal("21.00"))
                .status(OrderStatus.PROCESSED)
                .build();
    }
}

