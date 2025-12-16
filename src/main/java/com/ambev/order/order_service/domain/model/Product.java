package com.ambev.order.order_service.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @NotNull(message = "Pedido é obrigatório")
    private Order order;

    @Column(name = "name", nullable = false, length = 255)
    @NotBlank(message = "Nome do produto é obrigatório")
    private String name;

    @Column(name = "product_value", nullable = false, precision = 19, scale = 2)
    @NotNull(message = "Valor do produto é obrigatório")
    @Positive(message = "Valor do produto deve ser positivo")
    private BigDecimal value;

    @Column(name = "quantity", nullable = false)
    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private Integer quantity;
}

