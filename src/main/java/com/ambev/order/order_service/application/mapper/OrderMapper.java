package com.ambev.order.order_service.application.mapper;

import com.ambev.order.order_service.application.dto.OrderRequestDTO;
import com.ambev.order.order_service.application.dto.OrderResponseDTO;
import com.ambev.order.order_service.application.dto.ProductDTO;
import com.ambev.order.order_service.domain.model.Order;
import com.ambev.order.order_service.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

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

    OrderResponseDTO toDTO(Order entity);

    List<OrderResponseDTO> toDTOList(List<Order> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    Product toProductEntity(ProductDTO dto);

    ProductDTO toProductDTO(Product entity);

    List<ProductDTO> toProductDTOList(List<Product> products);
}

