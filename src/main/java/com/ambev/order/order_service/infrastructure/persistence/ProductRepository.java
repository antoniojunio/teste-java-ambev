package com.ambev.order.order_service.infrastructure.persistence;

import com.ambev.order.order_service.domain.model.Product;
import com.ambev.order.order_service.domain.repository.IProductRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, IProductRepository {

    @Override
    @Query("SELECT p FROM Product p WHERE p.order.id = :orderId")
    List<Product> findByOrderId(@Param("orderId") Long orderId);
}

