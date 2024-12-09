package se.sundsvall.myrepresentative.integration.minaombud.jwks;

import feign.Request;
import java.util.concurrent.TimeUnit;
import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import se.sundsvall.dept44.configuration.feign.FeignConfiguration;
import se.sundsvall.dept44.configuration.feign.FeignMultiCustomizer;
import se.sundsvall.myrepresentative.integration.minaombud.OmbudProperties;

/**
 * Separate config for the jwks endpoint with no authorization
 */
@Import(FeignConfiguration.class)
public class MinaOmbudJwksConfiguration {

	private final OmbudProperties ombudProperties;

	public MinaOmbudJwksConfiguration(OmbudProperties ombudProperties) {
		this.ombudProperties = ombudProperties;
	}

	@Bean
	FeignBuilderCustomizer feignBuilderCustomizer() {
		return FeignMultiCustomizer.create()
			.withRequestOptions(feignOptions())
			.composeCustomizersToOne();
	}

	Request.Options feignOptions() {
		return new Request.Options(
			ombudProperties.getConnectTimeout().toMillis(), TimeUnit.MILLISECONDS,
			ombudProperties.getReadTimeout().toMillis(), TimeUnit.MILLISECONDS,
			true);
	}
}
