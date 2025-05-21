package com.microservico.pedidoservice.config;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerGroupConfig {

    @Bean
    public GroupedOpenApi pedidosApi() {
        return GroupedOpenApi.builder()
                .group("pedidos")  // Nome do grupo exibido no Swagger UI
                .pathsToMatch("/pedidos/**")  // Endpoints que serão documentados
                .build();
    }

    // Exemplo de configuração para endpoints públicos (se necessário no futuro)
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/public/**")
                .addOperationCustomizer((operation, handlerMethod) -> {
                    if (operation.getOperationId() != null && operation.getOperationId().equals("register")) {
                        operation.addSecurityItem(new SecurityRequirement()); // Remove exigência de auth
                    }
                    return operation;
                })
                .build();
    }
}
