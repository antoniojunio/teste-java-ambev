package com.ambev.order.order_service.application.mapper;

import com.ambev.order.order_service.application.dto.OrderRequestDTO;
import com.ambev.order.order_service.application.dto.OrderResponseDTO;
import com.ambev.order.order_service.application.dto.ProductDTO;
import com.ambev.order.order_service.domain.model.Order;
import com.ambev.order.order_service.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.AfterMapping;

import java.util.List;
import java.util.stream.Collectors;

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
        if (dto.getProducts() != null) {
            List<Product> products = dto.getProducts().stream()
                    .map(this::toProductEntity)
                    .collect(Collectors.toList());
            products.forEach(product -> product.setOrder(order));
            order.setProducts(products);
        }
        return order;
    }

    OrderResponseDTO toDTO(Order entity);

    default OrderResponseDTO toDTOWithProducts(Order entity) {
        OrderResponseDTO dto = toDTO(entity);
        if (entity.getProducts() != null) {
            dto.setProducts(toProductDTOList(entity.getProducts()));
        }
        return dto;
    }

    List<OrderResponseDTO> toDTOList(List<Order> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    Product toProductEntity(ProductDTO dto);

    ProductDTO toProductDTO(Product entity);

    List<ProductDTO> toProductDTOList(List<Product> products);
}

