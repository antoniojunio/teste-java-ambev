package com.ambev.order.order_service.domain.repository;

import com.ambev.order.order_service.domain.model.Order;
import com.ambev.order.order_service.domain.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IOrderRepository {
    
    Order save(Order order);
    
    Optional<Order> findById(Long id);
    
    Optional<Order> findByExternalId(String externalId);
    
    boolean existsByExternalId(String externalId);
    
    void deleteById(Long id);
    
    Page<Order> findAll(Pageable pageable);
    
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
}

