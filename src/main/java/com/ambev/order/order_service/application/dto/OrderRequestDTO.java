package com.ambev.order.order_service.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {

    @NotBlank(message = "External ID é obrigatório")
    private String externalId;

    @NotEmpty(message = "Lista de produtos não pode estar vazia")
    @Valid
    private List<ProductDTO> products;
}

