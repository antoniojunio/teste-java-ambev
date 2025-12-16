package com.ambev.order.order_service.domain.service;

import com.ambev.order.order_service.domain.model.Order;
import com.ambev.order.order_service.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderValidationService {

    public void validate(Order order) {
        List<String> errors = new ArrayList<>();

        if (order == null) {
            throw new IllegalArgumentException("Pedido não pode ser nulo");
        }

        if (order.getExternalId() == null || order.getExternalId().trim().isEmpty()) {
            errors.add("External ID é obrigatório");
        }

        if (order.getProducts() == null || order.getProducts().isEmpty()) {
            errors.add("Pedido deve conter pelo menos um produto");
        } else {
            validateProducts(order.getProducts(), errors);
        }

        if (!errors.isEmpty()) {
            String errorMessage = String.join(", ", errors);
            throw new IllegalArgumentException("Erros de validação: %s".formatted(errorMessage)); // Java 21: String.formatted()
        }
    }


    private void validateProducts(List<Product> products, List<String> errors) {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int index = i + 1;

            if (product == null) {
                errors.add("Produto %d não pode ser nulo".formatted(index)); // Java 21: String.formatted()
                continue;
            }

            if (product.getName() == null || product.getName().trim().isEmpty()) {
                errors.add("Nome do produto %d é obrigatório".formatted(index));
            }

            if (product.getValue() == null || product.getValue().signum() <= 0) {
                errors.add("Valor do produto %d deve ser positivo".formatted(index));
            }

            if (product.getQuantity() == null || product.getQuantity() <= 0) {
                errors.add("Quantidade do produto %d deve ser maior que zero".formatted(index));
            }
        }
    }
}

