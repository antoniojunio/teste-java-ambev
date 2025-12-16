package com.ambev.order.order_service.application.dto;

import com.ambev.order.order_service.domain.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public record OrderResponseDTO(
    Long id,
    String externalId,
    BigDecimal totalValue,
    OrderStatus status,
    LocalDateTime createdAt,
    LocalDateTime processedAt,
    List<ProductDTO> products
) {}

