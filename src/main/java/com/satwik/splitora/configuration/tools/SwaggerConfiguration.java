package com.satwik.splitora.configuration.tools;

import com.satwik.splitora.constants.SecurityConstants;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Splitora API")
                        .version("1.0")
                        .description("API documentation for Splitora application"));
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {

        final String securitySchemeName = "bearerAuth";

        return openApi ->
            openApi.getPaths().forEach((s, pathItem) -> {
                boolean isSecured = SecurityConstants.WHITELISTED_URLS.stream().noneMatch(s::contains);
                if (isSecured) {
                    pathItem.readOperations().forEach(operation ->
                        operation.addSecurityItem(new SecurityRequirement().addList(securitySchemeName)));
                }
            });
    }
}
