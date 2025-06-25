package com.tikitaka.api.config;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.annotations.info.Info;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = "JWT",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
@OpenAPIDefinition(
    info = @Info(title = "API 문서", version = "v1"),
    security = { @SecurityRequirement(name = "JWT") }
)
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("로컬 개발 서버");

        Server devServer = new Server();
        devServer.setUrl("https://api.tikitaka.o-r.kr");
        devServer.setDescription("원격 개발 서버");
        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Tiki-Taka API 문서")
                        .description("API 명세서")
                        .version("v1.0")).servers(List.of(localServer, devServer));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/**", "/auth/**")
                .build();
    }
}

