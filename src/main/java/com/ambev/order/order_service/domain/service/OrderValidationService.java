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
            throw new IllegalArgumentException("Erros de validação: " + String.join(", ", errors));
        }
    }

    private void validateProducts(List<Product> products, List<String> errors) {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int index = i + 1;

            if (product == null) {
                errors.add(String.format("Produto %d não pode ser nulo", index));
                continue;
            }

            if (product.getName() == null || product.getName().trim().isEmpty()) {
                errors.add(String.format("Nome do produto %d é obrigatório", index));
            }

            if (product.getValue() == null || product.getValue().signum() <= 0) {
                errors.add(String.format("Valor do produto %d deve ser positivo", index));
            }

            if (product.getQuantity() == null || product.getQuantity() <= 0) {
                errors.add(String.format("Quantidade do produto %d deve ser maior que zero", index));
            }
        }
    }
}

