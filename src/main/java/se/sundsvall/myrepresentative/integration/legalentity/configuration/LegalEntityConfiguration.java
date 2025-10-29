package se.sundsvall.myrepresentative.integration.legalentity.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import se.sundsvall.dept44.configuration.feign.FeignConfiguration;
import se.sundsvall.dept44.configuration.feign.FeignMultiCustomizer;

@Import(FeignConfiguration.class)
@EnableConfigurationProperties(LegalEntityProperties.class)
public class LegalEntityConfiguration {

	public static final String CLIENT_ID = "legalentity";

	@Bean
	FeignBuilderCustomizer feignBuilderCustomizer(final LegalEntityProperties partyProperties, final ClientRegistrationRepository clientRegistrationRepository) {
		return FeignMultiCustomizer.create()
			.withRequestTimeoutsInSeconds(partyProperties.connectTimeout(), partyProperties.readTimeout())
			.withRetryableOAuth2InterceptorForClientRegistration(clientRegistrationRepository.findByRegistrationId(CLIENT_ID))
			.composeCustomizersToOne();
	}
}
