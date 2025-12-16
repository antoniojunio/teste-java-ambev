package com.ambev.order.order_service.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;


public record OrderRequestDTO(
    @NotBlank(message = "External ID é obrigatório")
    String externalId,

    @NotEmpty(message = "Lista de produtos não pode estar vazia")
    @Valid
    List<ProductDTO> products
) {}

