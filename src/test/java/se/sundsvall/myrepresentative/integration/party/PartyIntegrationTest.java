package se.sundsvall.myrepresentative.integration.party;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static generated.se.sundsvall.party.PartyType.ENTERPRISE;
import static generated.se.sundsvall.party.PartyType.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;

@ExtendWith(MockitoExtension.class)
class PartyIntegrationTest {

	@Mock
	private PartyClient partyClient;

	@InjectMocks
	private PartyIntegration partyIntegration;

	@AfterEach
	void tearDown() {
		verifyNoMoreInteractions(partyClient);
	}

	@Test
	void getOrganizationLegalId() {
		final var partyId = UUID.randomUUID().toString();
		final var optionalLegalId = Optional.of("5560269986");

		when(partyClient.getLegalIdByPartyId(MUNICIPALITY_ID, ENTERPRISE, partyId)).thenReturn(optionalLegalId);

		final var legalId = partyIntegration.getOrganizationLegalId(MUNICIPALITY_ID, partyId);

		assertThat(legalId).isEqualTo(optionalLegalId);
		verify(partyClient).getLegalIdByPartyId(MUNICIPALITY_ID, ENTERPRISE, partyId);
	}

	@Test
	void getOrganizationLegalIdNotFound() {
		final var partyId = UUID.randomUUID().toString();

		when(partyClient.getLegalIdByPartyId(MUNICIPALITY_ID, ENTERPRISE, partyId)).thenReturn(Optional.empty());

		final var result = partyIntegration.getOrganizationLegalId(MUNICIPALITY_ID, partyId);

		assertThat(result).isEmpty();
		verify(partyClient).getLegalIdByPartyId(MUNICIPALITY_ID, ENTERPRISE, partyId);
	}

	@Test
	void getPersonalLegalId() {
		final var partyId = UUID.randomUUID().toString();
		final var optionalLegalId = Optional.of("201001132396");

		when(partyClient.getLegalIdByPartyId(MUNICIPALITY_ID, PRIVATE, partyId)).thenReturn(optionalLegalId);

		final var legalId = partyIntegration.getPersonalLegalId(MUNICIPALITY_ID, partyId);

		assertThat(legalId).isEqualTo(optionalLegalId);
		verify(partyClient).getLegalIdByPartyId(MUNICIPALITY_ID, PRIVATE, partyId);
	}

	@Test
	void getPersonalLegalIdNotFound() {
		final var partyId = UUID.randomUUID().toString();

		when(partyClient.getLegalIdByPartyId(MUNICIPALITY_ID, PRIVATE, partyId)).thenReturn(Optional.empty());

		final var result = partyIntegration.getPersonalLegalId(MUNICIPALITY_ID, partyId);

		assertThat(result).isEmpty();
		verify(partyClient).getLegalIdByPartyId(MUNICIPALITY_ID, PRIVATE, partyId);
	}

}
