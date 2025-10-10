package com.connectfood.core.infrastructure.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI connectFoodOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Restaurant Management System - ConnectFood")
            .version("1.0.0")
            .description("API for user management (customers and restaurant owners) and login validation."))
        .servers(List.of(
            new Server().url("http://localhost:9090")
                .description("Local host")
        ));
  }
}
