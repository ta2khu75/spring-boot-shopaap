package dev.ta2khu75.shopapp.configurations;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "E-commerce api in Java Springboot", version = "1.0.0", description = "Ứng dụng shopaap để training"), servers = {
    @Server(url = "http://localhost:8080", description = "Local Development Service"),
    @Server(url = "http://45.117.179.16:8080", description = "Production Service")

})

@SecurityScheme(name = "bearer-key", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
@Configuration
public class OpenApiConfig {

}
