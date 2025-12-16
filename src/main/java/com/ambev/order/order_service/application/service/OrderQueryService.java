package com.ambev.order.order_service.application.service;

import com.ambev.order.order_service.application.dto.OrderResponseDTO;
import com.ambev.order.order_service.domain.model.Order;
import com.ambev.order.order_service.domain.model.OrderStatus;
import com.ambev.order.order_service.application.mapper.OrderMapper;
import com.ambev.order.order_service.domain.repository.IOrderRepository;
import com.ambev.order.order_service.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderQueryService {

    private final IOrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderResponseDTO findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Pedido não encontrado com ID: " + id));
        return orderMapper.toDTO(order);
    }

    public Page<OrderResponseDTO> findAll(Pageable pageable) {
        Page<Order> orders = (Page<Order>) orderRepository.findAll(pageable);
        return orders.map(orderMapper::toDTO);
    }

    public Page<OrderResponseDTO> findByStatus(OrderStatus status, Pageable pageable) {
        Page<Order> orders = orderRepository.findByStatus(status, pageable);
        return orders.map(orderMapper::toDTO);
    }
}

