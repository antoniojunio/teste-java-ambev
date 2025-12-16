package com.ambev.order.order_service.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;


public record ProductDTO(
    @NotBlank(message = "Nome do produto é obrigatório")
    String name,

    @NotNull(message = "Valor do produto é obrigatório")
    @Positive(message = "Valor do produto deve ser positivo")
    BigDecimal value,

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    Integer quantity
) {}

