package se.sundsvall.myrepresentative.service.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.nimbusds.jose.jwk.RSAKey;

import se.sundsvall.myrepresentative.MyRepresentatives;
import se.sundsvall.myrepresentative.api.model.jwks.Jwks;

@SpringBootTest(classes = { MyRepresentatives.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class JwksCacheTest {

	@Autowired
	private JwksCache jwksCache;

	@Test
	void addJwkAndMakeSureItsStored() {
		final RSAKey mockJWK = Mockito.mock(RSAKey.class);
		Mockito.when(mockJWK.getKeyID()).thenReturn("1234567890");
		jwksCache.addJwk(mockJWK);

		final Jwks jwks = jwksCache.getJwks();
		assertThat(jwks).isNotNull();
		assertThat(jwks.getKeys()).hasSize(1);
		assertThat(jwks.getKeys().stream()
			.allMatch(Objects::nonNull))
			.isTrue();
	}
}
