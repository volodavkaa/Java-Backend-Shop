package com.example.javabackend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.*;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "My API", version = "1.0", description = "Опис проєкту"),
        servers = @Server(url = "http://localhost:8080"),  // якщо локально
        security = @SecurityRequirement(name = "bearerAuth")
        // можна додати @Tag(name="AuthController", description="Тут опис") тощо
)
@SecurityScheme(
        name = "bearerAuth",                   // має збігатися з @SecurityRequirement(name="bearerAuth")
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"                   // лише описово, можна не вказувати
)
public class OpenApiConfig {
}
