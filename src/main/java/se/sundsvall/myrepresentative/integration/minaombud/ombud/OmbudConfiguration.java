package se.sundsvall.myrepresentative.integration.minaombud.ombud;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.cloud.openfeign.FeignBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import feign.Request;
import se.sundsvall.dept44.configuration.feign.FeignConfiguration;
import se.sundsvall.dept44.configuration.feign.FeignMultiCustomizer;
import se.sundsvall.dept44.configuration.feign.interceptor.OAuth2RequestInterceptor;
import se.sundsvall.myrepresentative.integration.minaombud.OmbudProperties;

@Import(FeignConfiguration.class)
public class OmbudConfiguration {

	private static final String REGISTRATION_ID = "minaombud";
	private static final String X_SERVICE_NAME = "X-Service-Name";
	private static final String X_SERVICE_VALUE = "myrepresentatives";
	private static final String SCOPE_USER_SELF = "user:self";    // Used when getting token

	private final OmbudProperties ombudProperties;

	public OmbudConfiguration(OmbudProperties ombudProperties) {
		this.ombudProperties = ombudProperties;
	}

	@Bean
	FeignBuilderCustomizer feignBuilderCustomizer() {
		return FeignMultiCustomizer.create()
			.withRequestOptions(feignOptions())
			.withRequestInterceptor(requst -> requst.header(X_SERVICE_NAME, X_SERVICE_VALUE)) // Add required static header for bolagsverket / minaombud
			.withRetryableOAuth2InterceptorForClientRegistration(clientRegistration(), Set.of(SCOPE_USER_SELF))
			.composeCustomizersToOne();
	}

	private ClientRegistration clientRegistration() {
		return ClientRegistration.withRegistrationId(REGISTRATION_ID)
			.tokenUri(ombudProperties.getOauth2TokenUrl())
			.clientId(ombudProperties.getOauth2ClientId())
			.clientSecret(ombudProperties.getOauth2ClientSecret())
			.authorizationGrantType(new AuthorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()))
			.build();
	}

	Request.Options feignOptions() {
		return new Request.Options(
			ombudProperties.getConnectTimeout().toMillis(), TimeUnit.MILLISECONDS,
			ombudProperties.getReadTimeout().toMillis(), TimeUnit.MILLISECONDS,
			true);
	}
}
