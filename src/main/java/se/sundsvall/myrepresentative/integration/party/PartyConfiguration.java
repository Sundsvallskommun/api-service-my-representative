package se.sundsvall.myrepresentative.integration.party;

import feign.Request;
import feign.codec.ErrorDecoder;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import se.sundsvall.dept44.configuration.feign.FeignConfiguration;
import se.sundsvall.dept44.configuration.feign.FeignMultiCustomizer;
import se.sundsvall.dept44.configuration.feign.decoder.ProblemErrorDecoder;

@Import(FeignConfiguration.class)
public class PartyConfiguration {

	static final String CLIENT_ID = "party";

	private final PartyProperties partyProperties;

	public PartyConfiguration(final PartyProperties partyProperties) {
		this.partyProperties = partyProperties;
	}

	@Bean
	FeignBuilderCustomizer feignBuilderCustomizer() {
		return FeignMultiCustomizer.create()
			.withErrorDecoder(errorDecoder())
			.withRequestOptions(feignOptions())
			.withRetryableOAuth2InterceptorForClientRegistration(clientRegistration())
			.composeCustomizersToOne();
	}

	private ClientRegistration clientRegistration() {
		return ClientRegistration.withRegistrationId(CLIENT_ID)
			.tokenUri(partyProperties.getOauth2TokenUrl())
			.clientId(partyProperties.getOauth2ClientId())
			.clientSecret(partyProperties.getOauth2ClientSecret())
			.authorizationGrantType(new AuthorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()))
			.build();
	}

	ErrorDecoder errorDecoder() {
		// We want to return 404 as a 404.
		return new ProblemErrorDecoder(CLIENT_ID, List.of(HttpStatus.NOT_FOUND.value()));
	}

	Request.Options feignOptions() {
		return new Request.Options(
			partyProperties.getConnectTimeout().toMillis(), TimeUnit.MILLISECONDS,
			partyProperties.getReadTimeout().toMillis(), TimeUnit.MILLISECONDS,
			true);
	}
}
