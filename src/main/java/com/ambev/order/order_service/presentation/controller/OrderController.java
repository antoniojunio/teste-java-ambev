package com.ambev.order.order_service.presentation.controller;

import com.ambev.order.order_service.application.dto.OrderRequestDTO;
import com.ambev.order.order_service.application.dto.OrderResponseDTO;
import com.ambev.order.order_service.application.service.OrderApplicationService;
import com.ambev.order.order_service.application.service.OrderQueryService;
import com.ambev.order.order_service.domain.model.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Orders", description = "API para gerenciamento de pedidos")
public class OrderController {

    private final OrderApplicationService applicationService;
    private final OrderQueryService queryService;

    @PostMapping
    @Operation(summary = "Criar pedido", description = "Recebe um pedido do Produto Externo A e cria um novo pedido no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso",
                    content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Pedido duplicado")
    })
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        log.info("Recebendo pedido do Produto Externo A: {}", request.getExternalId());
        OrderResponseDTO response = applicationService.processOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico pelo ID para o Produto Externo B")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado",
                    content = @Content(schema = @Schema(implementation = OrderResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    })
    public ResponseEntity<OrderResponseDTO> getOrder(
            @Parameter(description = "ID do pedido", required = true) @PathVariable Long id) {
        log.info("Consultando pedido para Produto Externo B: {}", id);
        OrderResponseDTO response = queryService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar pedidos", description = "Lista todos os pedidos com paginação")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    public ResponseEntity<Page<OrderResponseDTO>> listOrders(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Listando pedidos - página: {}, tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<OrderResponseDTO> orders = queryService.findAll(pageable);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Filtrar pedidos por status", description = "Lista pedidos filtrados por status (PENDING, PROCESSED, FAILED)")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos filtrados retornada com sucesso")
    public ResponseEntity<Page<OrderResponseDTO>> getOrdersByStatus(
            @Parameter(description = "Status do pedido (PENDING, PROCESSED, FAILED)", required = true)
            @PathVariable OrderStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Filtrando pedidos por status: {}", status);
        Page<OrderResponseDTO> orders = queryService.findByStatus(status, pageable);
        return ResponseEntity.ok(orders);
    }
}

