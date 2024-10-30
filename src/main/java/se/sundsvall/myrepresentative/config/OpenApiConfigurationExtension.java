package se.sundsvall.myrepresentative.config;

import java.util.Optional;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Operation;

/**
 * Disable the authentication for the "/jwks"-endpoint since it needs to be public for Bolagsverket.
 */
@Configuration
public class OpenApiConfigurationExtension {

	public static final String JWKS_ENDPOINT = "/jwks";

	@Bean
	public OpenApiCustomizer addNoAuthEndpoint() {
		return openApi -> Optional.ofNullable(openApi.getPaths().get(JWKS_ENDPOINT))
			.flatMap(openApiPath -> Optional.ofNullable(openApiPath.getGet())).ifPresent(this::extendOperation);
	}

	void extendOperation(Operation operation) {
		operation.addExtension("x-auth-type", "None");
		operation.addExtension("x-throttling-tier", "Unlimited");
		operation.addExtension("x-wso2-mutual-ssl", "Optional");
	}
}
