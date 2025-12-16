package com.ambev.order.order_service.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Order Service API")
                        .version("1.0.0")
                        .description("API para gerenciamento de pedidos - Sistema de recebimento de pedidos do Produto Externo A e disponibilização para Produto Externo B")
                        .contact(new Contact()
                                .name("Order Service Team")
                                .email("order-service@ambev.com")));
    }
}

