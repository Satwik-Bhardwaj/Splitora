package com.satwik.splitora.configuration.tools;

import com.satwik.splitora.constants.SecurityConstants;
import com.satwik.splitora.persistence.dto.ErrorDetails;
import com.satwik.splitora.persistence.dto.ErrorResponseModel;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Splitora API")
                        .version("1.0")
                        .description("API documentation for Splitora application"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    @Bean
    public OpenApiCustomizer openApiCustomizer() {

        ResolvedSchema errorDetailsSchema = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(ErrorDetails.class));

        ResolvedSchema errorResponseModelSchema = ModelConverters.getInstance()
                .resolveAsResolvedSchema(new AnnotatedType(ErrorResponseModel.class));
        Content contentWithExample400 =
                new Content().addMediaType("application/json", new MediaType().schema(errorResponseModelSchema.schema)
                        .example(new ErrorResponseModel("BAD_REQUEST", "Invalid input", LocalDateTime.now(), new ErrorDetails(400, "Validation failed for the request"))));
        Content contentWithExample401 =
                new Content().addMediaType("application/json", new MediaType().schema(errorResponseModelSchema.schema)
                        .example(new ErrorResponseModel("UNAUTHORIZED", "Unauthorized access", LocalDateTime.now(), new ErrorDetails(401, "Authentication failed"))));
        Content contentWithExample403 =
                new Content().addMediaType("application/json", new MediaType().schema(errorResponseModelSchema.schema)
                        .example(new ErrorResponseModel("FORBIDDEN", "Access denied", LocalDateTime.now(), new ErrorDetails(403, "You do not have permission to access this resource"))));
        Content contentWithExample404 =
                new Content().addMediaType("application/json", new MediaType().schema(errorResponseModelSchema.schema)
                        .example(new ErrorResponseModel("NOT_FOUND", "Resource not found", LocalDateTime.now(), new ErrorDetails(404, "The requested resource was not found"))));
        Content contentWithExample500 =
                new Content().addMediaType("application/json", new MediaType().schema(errorResponseModelSchema.schema)
                        .example(new ErrorResponseModel("INTERNAL_SERVER_ERROR", "An unexpected error occurred", LocalDateTime.now(), new ErrorDetails(500, "Internal server error"))));

        final String securitySchemeName = "bearerAuth";

        return openApi -> {
            openApi.schema("ErrorDetails", errorDetailsSchema.schema);
            openApi.getPaths().forEach((path, pathItem) -> {
                pathItem.readOperations().forEach(operation -> operation.getResponses()
                        .addApiResponse("400", new ApiResponse()
                                .description("Bad Request")
                                .content(contentWithExample400))
                        .addApiResponse("401", new ApiResponse()
                                .description("Unauthorized")
                                .content(contentWithExample401))
                        .addApiResponse("403", new ApiResponse()
                                .description("Forbidden")
                                .content(contentWithExample403))
                        .addApiResponse("404", new ApiResponse()
                                .description("Not Found")
                                .content(contentWithExample404))
                        .addApiResponse("500", new ApiResponse()
                                .description("Internal Server Error")
                                .content(contentWithExample500)));

                boolean isSecured = SecurityConstants.WHITELISTED_URLS.stream().noneMatch(path::contains);
                if (isSecured) {
                    pathItem.readOperations().forEach(operation ->
                            operation.addSecurityItem(new SecurityRequirement().addList(securitySchemeName)));
                }
            });
        };
    }
}
