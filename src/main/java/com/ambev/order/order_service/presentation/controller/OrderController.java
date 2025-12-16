package com.ambev.order.order_service.presentation.controller;

import com.ambev.order.order_service.application.dto.OrderRequestDTO;
import com.ambev.order.order_service.application.dto.OrderResponseDTO;
import com.ambev.order.order_service.application.service.OrderApplicationService;
import com.ambev.order.order_service.application.service.OrderQueryService;
import com.ambev.order.order_service.domain.model.OrderStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderApplicationService applicationService;
    private final OrderQueryService queryService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        log.info("Recebendo pedido do Produto Externo A: {}", request.getExternalId());
        OrderResponseDTO response = applicationService.processOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable Long id) {
        log.info("Consultando pedido para Produto Externo B: {}", id);
        OrderResponseDTO response = queryService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> listOrders(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Listando pedidos - página: {}, tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<OrderResponseDTO> orders = queryService.findAll(pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<OrderResponseDTO>> getOrdersByStatus(
            @PathVariable OrderStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Filtrando pedidos por status: {}", status);
        Page<OrderResponseDTO> orders = queryService.findByStatus(status, pageable);
        return ResponseEntity.ok(orders);
    }
}

