package com.ambev.order.order_service.domain.repository;

import com.ambev.order.order_service.domain.model.Product;

import java.util.List;

public interface IProductRepository {
    
    List<Product> findByOrderId(Long orderId);
}

