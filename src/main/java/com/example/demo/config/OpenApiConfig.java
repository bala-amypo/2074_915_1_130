package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // API basic info
                .info(new Info()
                        .title("Warranty Claim Fraud Detector API")
                        .version("1.0")
                        .description("API for device ownership, warranty claims, fraud detection, and alert management"))

                // Server configuration
                .servers(List.of(
                        new Server().url("https://9089.408procr.amypo.ai/")
                ))

                // Security requirement
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))

                // Security scheme
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
