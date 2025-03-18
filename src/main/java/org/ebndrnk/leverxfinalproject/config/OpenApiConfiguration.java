package org.ebndrnk.leverxfinalproject.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * AppConfiguration
 *
 * This configuration class sets up the application's beans, including the ModelMapper
 * for object mapping and OpenAPI configuration for API documentation. It also configures
 * the security scheme for JWT-based authentication.
 */
@Configuration
@EnableAsync(proxyTargetClass=true)
public class OpenApiConfiguration {


    /**
     * Configures the security scheme for API key authentication using JWT.
     *
     * @return the configured SecurityScheme instance
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    /**
     * Creates an OpenAPI bean for API documentation.
     * The server URL is injected from the property 'site.domain.url'.
     *
     * @param api the base API URL from configuration
     * @return a configured OpenAPI instance with security and server details
     */
    @Bean
    OpenAPI prodOpenAPI(@Value("${site.domain.url}") String api) {
        return new OpenAPI()
                .addServersItem(new Server().url(api))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info().title("LeverX Project"));
    }


}
