package com.ambev.order.order_service.domain.service;

import com.ambev.order.order_service.domain.model.Order;
import com.ambev.order.order_service.domain.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class OrderCalculationService {

    public BigDecimal calculateTotal(Order order) {
        if (order == null || order.getProducts() == null || order.getProducts().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return order.getProducts().stream()
                .map(this::calculateProductTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateProductTotal(Product product) {
        if (product == null || product.getValue() == null || product.getQuantity() == null) {
            return BigDecimal.ZERO;
        }

        return product.getValue()
                .multiply(BigDecimal.valueOf(product.getQuantity()))
                .setScale(2, RoundingMode.HALF_UP);
    }
}

