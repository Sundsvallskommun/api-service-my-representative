package se.sundsvall.myrepresentative.service.authorities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import se.sundsvall.myrepresentative.TestObjectFactory;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesRequest;

import generated.se.sundsvall.minaombud.HamtaFullmakterRequest;

class AuthoritiesRequestMapperTest {

	private final AuthoritiesRequestMapper requestMapper = new AuthoritiesRequestMapper();

	@Test
	void testCreateBehorigheterRequest() {
		AuthoritiesRequest authoritiesRequest = TestObjectFactory.createAuthorityRequest();
		HamtaFullmakterRequest fullmakterRequest = requestMapper.createFullmakterRequest(authoritiesRequest);

		assertThat(fullmakterRequest.getFullmaktshavare().getId()).isEqualTo("acquirerLegalId");
		assertThat(fullmakterRequest.getFullmaktshavare().getTyp()).isEqualTo("orgnr");

		assertThat(fullmakterRequest.getFullmaktsgivare().getId()).isEqualTo("issuerLegalId");
		assertThat(fullmakterRequest.getFullmaktsgivare().getTyp()).isEqualTo("orgnr");
	}

	@Test
	void testMissingIssuer_shouldNotBeMapped() {
		AuthoritiesRequest authoritiesRequest = TestObjectFactory.createAuthorityRequest();
		authoritiesRequest.setAuthorityIssuer(null);

		HamtaFullmakterRequest fullmakterRequest = requestMapper.createFullmakterRequest(authoritiesRequest);

		assertThat(fullmakterRequest.getFullmaktshavare().getId()).isEqualTo("acquirerLegalId");
		assertThat(fullmakterRequest.getFullmaktshavare().getTyp()).isEqualTo("orgnr");

		assertThat(fullmakterRequest.getFullmaktsgivare()).isNull();

	}
}
