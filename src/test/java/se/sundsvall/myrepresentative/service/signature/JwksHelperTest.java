package se.sundsvall.myrepresentative.service.signature;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.sundsvall.myrepresentative.TestObjectFactory.populateJWKSet;
import static se.sundsvall.myrepresentative.service.mandates.MandatesRequestMapper.THIRD_PARTY;

import java.text.ParseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.jwk.JWK;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.ThrowableProblem;

import se.sundsvall.dept44.test.annotation.resource.Load;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;
import se.sundsvall.myrepresentative.integration.minaombud.jwks.MinaOmbudJwksIntegration;

@ExtendWith({
	MockitoExtension.class, ResourceLoaderExtension.class
})
class JwksHelperTest {

	// A header so we can test parsing ({"kid":"18d461edff91adc8a2b3cfd01d71ff703e0afb20","alg":"RS256"})
	private static final String PROTECTED_HEADER = "eyJraWQiOiIxOGQ0NjFlZGZmOTFhZGM4YTJiM2NmZDAxZDcxZmY3MDNlMGFmYjIwIiwiYWxnIjoiUlMyNTYifQ";

	@Mock
	private MinaOmbudJwksIntegration mockJwksClient;

	@InjectMocks
	private JwksHelper jwksHelper;

	private String jwks;

	@BeforeEach
	void setUp(@Load(value = "junit/jwks.json", as = Load.ResourceType.STRING) String jwks) {
		this.jwks = jwks;
	}

	@Test
	void getJWKFromProtectedHeader_shouldReturnJWK_whenFound() throws ParseException, JsonProcessingException {
		when(mockJwksClient.getJwks(THIRD_PARTY)).thenReturn(populateJWKSet(jwks));

		JWK jwkFromProtectedHeader = jwksHelper.getJWKFromProtectedHeader(PROTECTED_HEADER);
		assertThat(jwkFromProtectedHeader.getKeyID()).isEqualTo("18d461edff91adc8a2b3cfd01d71ff703e0afb20");
		verify(mockJwksClient, times(1)).getJwks(THIRD_PARTY);
	}

	@Test
	void getJWKFromProtectedHeader_shouldThrowException_whenNotFound() throws ParseException, JsonProcessingException {
		when(mockJwksClient.getJwks(THIRD_PARTY)).thenReturn(populateJWKSet(jwks));

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> jwksHelper.getJWKFromProtectedHeader("eyJraWQiOiJuby1zb3VwLWZvci15b3UiLCJhbGciOiJSUzI1NiJ9"))
			.withMessage("Couldn't verify response from bolagsverket");

		verify(mockJwksClient, times(2)).getJwks(THIRD_PARTY);
	}

	@Test
	void checkForCachedJwk_shouldDoNothingWhenFound() throws ParseException, JsonProcessingException {
		// Populate the jwks
		when(mockJwksClient.getJwks(THIRD_PARTY)).thenReturn(populateJWKSet(jwks));
		jwksHelper.populateJwksIfMissing();

		jwksHelper.validateKidIsCached("18d461edff91adc8a2b3cfd01d71ff703e0afb20");

		// 1 time for when the test populates the cache, no more if it's found, which it is.
		verify(mockJwksClient, times(1)).getJwks(THIRD_PARTY);
	}

	@Test
	void isKidMissingInKeySet_shouldReturnFalse_whenFound() throws ParseException, JsonProcessingException {
		// Populate the jwks
		when(mockJwksClient.getJwks(THIRD_PARTY)).thenReturn(populateJWKSet(jwks));
		jwksHelper.populateJwksIfMissing();

		boolean kidMissingInKeySet = jwksHelper.isKidMissingInKeySet("18d461edff91adc8a2b3cfd01d71ff703e0afb20");
		assertThat(kidMissingInKeySet).isFalse();
	}

	@Test
	void isKidMissingInKeySet_shouldReturnTrue_whenNotFound() throws ParseException, JsonProcessingException {
		// Populate the jwks
		when(mockJwksClient.getJwks(THIRD_PARTY)).thenReturn(populateJWKSet(jwks));
		jwksHelper.populateJwksIfMissing();

		boolean kidMissingInKeySet = jwksHelper.isKidMissingInKeySet("should-not-be-found");
		assertThat(kidMissingInKeySet).isTrue();
	}

	@Test
	void getKidFromProtectedHeader() throws JsonProcessingException, ParseException {
		when(mockJwksClient.getJwks(THIRD_PARTY)).thenReturn(populateJWKSet(jwks));
		jwksHelper.populateJwksIfMissing();

		String kidFromProtectedHeader = jwksHelper.getKidFromProtectedHeader(PROTECTED_HEADER);
		assertThat(kidFromProtectedHeader).isEqualTo("18d461edff91adc8a2b3cfd01d71ff703e0afb20");
	}

	@Test
	void populateJwksIfMissing() throws ParseException, JsonProcessingException {
		when(mockJwksClient.getJwks(THIRD_PARTY)).thenReturn(populateJWKSet(jwks));
		jwksHelper.populateJwksIfMissing();
		verify(mockJwksClient).getJwks(THIRD_PARTY);
	}
}
