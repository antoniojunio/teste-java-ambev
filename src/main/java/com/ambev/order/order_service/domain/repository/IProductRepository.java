package com.ambev.order.order_service.domain.repository;

import com.ambev.order.order_service.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductRepository {
    
    Product save(Product product);
    
    List<Product> saveAll(List<Product> products);
    
    Optional<Product> findById(Long id);
    
    List<Product> findByOrderId(Long orderId);
    
    void deleteById(Long id);
}

