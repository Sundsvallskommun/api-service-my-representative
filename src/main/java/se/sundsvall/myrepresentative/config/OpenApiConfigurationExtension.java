package se.sundsvall.myrepresentative.config;

import static se.sundsvall.myrepresentative.api.RepresentativesResource.JWKS_ENDPOINT;

import java.util.Optional;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Operation;

/**
 * Disable the authentication for the "/jwks"-endpoint since it needs to be public for Bolagsverket.
 */
@Configuration
public class OpenApiConfigurationExtension {

    @Bean
    public OpenApiCustomiser addNoAuthEndpoint() {
        return openApi -> Optional.ofNullable(openApi.getPaths().get(JWKS_ENDPOINT))
                .flatMap(openApiPath -> Optional.ofNullable(openApiPath.getGet())).ifPresent(this::extendOperation);
    }

    void extendOperation(Operation operation) {
        operation.addExtension("x-auth-type", "None");
        operation.addExtension("x-throttling-tier", "Unlimited");
        operation.addExtension("x-wso2-mutual-ssl", "Optional");
    }
}
