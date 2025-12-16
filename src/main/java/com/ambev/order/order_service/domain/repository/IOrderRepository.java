package com.ambev.order.order_service.domain.repository;

import com.ambev.order.order_service.domain.model.Order;
import com.ambev.order.order_service.domain.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IOrderRepository {
    
    Optional<Order> findByExternalId(String externalId);
    
    boolean existsByExternalId(String externalId);
    
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}

