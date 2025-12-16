package com.ambev.order.order_service.infrastructure.persistence;

import com.ambev.order.order_service.domain.model.Order;
import com.ambev.order.order_service.domain.model.OrderStatus;
import com.ambev.order.order_service.domain.repository.IOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, IOrderRepository {

    @Override
    @EntityGraph(attributePaths = {"products"}) 
    @Query("SELECT o FROM Order o WHERE o.externalId = :externalId")
    Optional<Order> findByExternalId(@Param("externalId") String externalId);

    @Override
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM Order o WHERE o.externalId = :externalId")
    boolean existsByExternalId(@Param("externalId") String externalId);

    @Override
    @EntityGraph(attributePaths = {"products"}) 
    @Query("SELECT o FROM Order o WHERE o.status = :status")
    Page<Order> findByStatus(@Param("status") OrderStatus status, Pageable pageable);
    
    @EntityGraph(attributePaths = {"products"})
    @Query("SELECT o FROM Order o WHERE o.id = :id")
    Optional<Order> findByIdWithProducts(@Param("id") Long id);
}

