package com.ambev.order.order_service.infrastructure.messaging;

import com.ambev.order.order_service.application.dto.OrderResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.ambev.order.order_service.infrastructure.config.RabbitMQConfig.ORDER_EXCHANGE;
import static com.ambev.order.order_service.infrastructure.config.RabbitMQConfig.ORDER_ROUTING_KEY;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishOrderProcessed(OrderResponseDTO order) {
        try {
            rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_ROUTING_KEY, order);
            log.info("Evento de pedido processado publicado: {}", order.id()); 
        } catch (Exception e) {
            log.error("Erro ao publicar evento de pedido processado: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao publicar evento", e);
        }
    }
}

