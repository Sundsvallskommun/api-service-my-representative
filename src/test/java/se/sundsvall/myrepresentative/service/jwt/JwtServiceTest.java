package se.sundsvall.myrepresentative.service.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.service.jwt.JwtService.ALGORITHM;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import se.sundsvall.myrepresentative.api.model.jwks.Jwks;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
@DirtiesContext
class JwtServiceTest {

	@Autowired
	private JwtService jwtService;

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	private int port;

	@Test
	void testCreateJwtAndVerifyUsingJwksEndpoint() throws Exception {
		final String signedJwt = jwtService.createSignedJwt("1234567890");

		final Jwks jwks = restTemplate.getForObject("http://localhost:" + port + "/" + MUNICIPALITY_ID + "/jwks", Jwks.class);

		final Map<String, Object> jwkMap = jwks.getKeys().stream().findFirst().get(); // We will only have one entry in this map
		final JWK jwk = JWK.parse(jwkMap);    // Parse the JWK from the map

		final RSASSAVerifier rsassaVerifier = new RSASSAVerifier((RSAKey) jwk);   // Create a verifier from the JWK
		final JWSObject jwsObject = JWSObject.parse(signedJwt);   // Parse the signed JWT

		// Verify the signature
		assertThat(jwsObject.verify(rsassaVerifier)).isTrue();
	}

	@Test
	void testCreateJwtAndVerifyHeaderAndPayload() throws Exception {
		final String signedJwt = jwtService.createSignedJwt("1234567890");
		final SignedJWT parse = SignedJWT.parse(signedJwt);

		final JWSHeader header = parse.getHeader();
		assertThat(header.getAlgorithm()).hasToString(ALGORITHM.getName());
		assertThat(header.getKeyID()).isNotNull();

		final JWTClaimsSet jwtClaimsSet = parse.getJWTClaimsSet();
		assertThat(jwtClaimsSet.getAudience().get(0)).isEqualTo("test-audience");
		assertThat(jwtClaimsSet.getIssuer()).isEqualTo("test-issuer");
		assertThat(jwtClaimsSet.getSubject()).isNotNull();
		assertThat(jwtClaimsSet.getStringClaim("https://claims.oidc.se/1.0/personalNumber")).isEqualTo("1234567890");
		assertThat(jwtClaimsSet.getExpirationTime()).isNotNull();
		assertThat(jwtClaimsSet.getIssueTime()).isNotNull();
	}
}
