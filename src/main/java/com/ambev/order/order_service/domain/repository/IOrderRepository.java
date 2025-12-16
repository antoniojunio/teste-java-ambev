package com.ambev.order.order_service.domain.repository;

import com.ambev.order.order_service.domain.model.Order;
import com.ambev.order.order_service.domain.model.OrderStatus;

import java.util.Optional;

public interface IOrderRepository {
    
    Order save(Order order);
    
    Optional<Order> findById(Long id);
    
    Optional<Order> findByExternalId(String externalId);
    
    boolean existsByExternalId(String externalId);
    
    void deleteById(Long id);
}

