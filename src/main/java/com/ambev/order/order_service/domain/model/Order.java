package com.ambev.order.order_service.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders", 
       uniqueConstraints = @UniqueConstraint(name = "uk_order_external_id", columnNames = "external_id"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false, unique = true, length = 100)
    @NotNull(message = "External ID é obrigatório")
    private String externalId;

    @Column(name = "total_value", nullable = false, precision = 19, scale = 2)
    @NotNull(message = "Valor total é obrigatório")
    @PositiveOrZero(message = "Valor total deve ser positivo ou zero")
    private BigDecimal totalValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @NotNull(message = "Status é obrigatório")
    private OrderStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Version
    @Column(name = "version")
    private Long version;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = OrderStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (status == OrderStatus.PROCESSED && processedAt == null) {
            processedAt = LocalDateTime.now();
        }
    }
}

