package com.ambev.order.order_service.application.service;

import com.ambev.order.order_service.application.dto.OrderRequestDTO;
import com.ambev.order.order_service.application.dto.OrderResponseDTO;
import com.ambev.order.order_service.application.mapper.OrderMapper;
import com.ambev.order.order_service.domain.model.Order;
import com.ambev.order.order_service.domain.model.OrderStatus;
import com.ambev.order.order_service.domain.model.Product;
import com.ambev.order.order_service.domain.repository.IOrderRepository;
import com.ambev.order.order_service.domain.repository.IProductRepository;
import com.ambev.order.order_service.domain.service.OrderCalculationService;
import com.ambev.order.order_service.domain.service.OrderValidationService;
import com.ambev.order.order_service.exception.DuplicateOrderException;
import com.ambev.order.order_service.infrastructure.cache.DistributedLockService;
import com.ambev.order.order_service.infrastructure.cache.RedisDuplicateService;
import com.ambev.order.order_service.infrastructure.messaging.OrderEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class OrderApplicationService {

    private final IOrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderCalculationService calculationService;
    private final OrderValidationService validationService;
    private final RedisDuplicateService duplicateService;
    private final DistributedLockService lockService;
    private final OrderEventPublisher eventPublisher;

    @Transactional
    public OrderResponseDTO processOrder(OrderRequestDTO request) {
        String externalId = request.getExternalId();
        
        if (!lockService.tryLock(externalId)) {
            throw new DuplicateOrderException("Pedido já está sendo processado: " + externalId);
        }

        try {
            if (duplicateService.isDuplicate(externalId) || orderRepository.existsByExternalId(externalId)) {
                throw new DuplicateOrderException("Pedido duplicado: " + externalId);
            }

            Order order = orderMapper.toEntityWithProducts(request);
            validationService.validate(order);

            order.setTotalValue(calculationService.calculateTotal(order));
            order.setStatus(OrderStatus.PROCESSED);

            Order saved = orderRepository.save(order);
            duplicateService.markAsProcessed(externalId);

            OrderResponseDTO response = orderMapper.toDTOWithProducts(saved);
            eventPublisher.publishOrderProcessed(response);

            log.info("Pedido processado com sucesso: {}", saved.getId());
            return response;

        } finally {
            lockService.releaseLock(externalId);
        }
    }
}

