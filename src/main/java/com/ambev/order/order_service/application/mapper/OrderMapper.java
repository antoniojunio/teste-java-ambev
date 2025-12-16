package com.ambev.order.order_service.application.mapper;

import com.ambev.order.order_service.application.dto.OrderRequestDTO;
import com.ambev.order.order_service.application.dto.OrderResponseDTO;
import com.ambev.order.order_service.application.dto.ProductDTO;
import com.ambev.order.order_service.domain.model.Order;
import com.ambev.order.order_service.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.AfterMapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalValue", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "processedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "products", ignore = true)
    Order toEntity(OrderRequestDTO dto);

    default Order toEntityWithProducts(OrderRequestDTO dto) {
        Order order = toEntity(dto);
        if (dto.products() != null) {
            List<Product> products = dto.products().stream()
                    .map(this::toProductEntity)
                    .toList(); 
            products.forEach(product -> product.setOrder(order));
            order.setProducts(products);
        }
        return order;
    }

    default OrderResponseDTO toDTO(Order entity) {
        if (entity == null) {
            return null;
        }
        
        List<ProductDTO> products = entity.getProducts() != null 
            ? toProductDTOList(entity.getProducts()) 
            : List.of();
        
        return new OrderResponseDTO(
            entity.getId(),
            entity.getExternalId(),
            entity.getTotalValue(),
            entity.getStatus(),
            entity.getCreatedAt(),
            entity.getProcessedAt(),
            products
        );
    }

    List<OrderResponseDTO> toDTOList(List<Order> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    Product toProductEntity(ProductDTO dto);

    ProductDTO toProductDTO(Product entity);

    List<ProductDTO> toProductDTOList(List<Product> products);
}

