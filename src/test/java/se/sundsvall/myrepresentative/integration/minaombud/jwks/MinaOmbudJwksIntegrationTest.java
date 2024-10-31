package se.sundsvall.myrepresentative.integration.minaombud.jwks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.ThrowableProblem;

import se.sundsvall.dept44.test.annotation.resource.Load;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;

import generated.se.sundsvall.minaombud.JwkSet;

@ExtendWith({
	MockitoExtension.class, ResourceLoaderExtension.class
})
class MinaOmbudJwksIntegrationTest {

	@Mock
	private MinaOmbudJwksClient mockIntegration;

	@InjectMocks
	private MinaOmbudJwksIntegration client;

	@Test
	void testGetJwksAndConvertToNimbusJoseJwks(@Load(value = "junit/jwks.json", as = Load.ResourceType.STRING) String jwks) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JwkSet jwkSet = mapper.readValue(jwks, JwkSet.class);

		when(mockIntegration.getJwks(anyString())).thenReturn(jwkSet);
		JWKSet result = client.getJwks("test");

		verify(mockIntegration).getJwks("test");
		assertThat(result).isNotNull();
		assertThat(result.getKeys()).hasSize(1);
	}

	@Test
	void testParsingFails_shouldThrowException(@Load(value = "junit/faulty-algorithm-jwks.json", as = Load.ResourceType.STRING) String faultyJwks) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		JwkSet jwkSet = mapper.readValue(faultyJwks, JwkSet.class);

		when(mockIntegration.getJwks(anyString())).thenReturn(jwkSet);

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> client.getJwks("test"))
			.withMessage("Couldn't parse JWK set from Mina Ombud");

		verify(mockIntegration).getJwks("test");
	}
}
