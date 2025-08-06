package se.sundsvall.myrepresentative.integration.party;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;

import generated.se.sundsvall.party.PartyType;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

@ExtendWith(MockitoExtension.class)
class PartyIntegrationTest {

	@Mock
	private PartyClient mockPartyClient;

	@InjectMocks
	private PartyIntegration partyIntegration;

	@Test
	void getPartyIdFromEnterpriseLegalId() {
		when(mockPartyClient.getPartyId(MUNICIPALITY_ID, PartyType.ENTERPRISE, "1234")).thenReturn(Optional.of("abc123"));
		String partyIdFromLegalId = partyIntegration.getPartyIdFromLegalId(MUNICIPALITY_ID, "1234", "orgnr");
		assertThat(partyIdFromLegalId).isEqualTo("abc123");
	}

	@Test
	void getPartyIdFromPrivateLegalId() {
		when(mockPartyClient.getPartyId(MUNICIPALITY_ID, PartyType.PRIVATE, "1234")).thenReturn(Optional.of("abc123"));
		String partyIdFromLegalId = partyIntegration.getPartyIdFromLegalId(MUNICIPALITY_ID, "1234", "pnr");
		assertThat(partyIdFromLegalId).isEqualTo("abc123");
	}

	@Test
	void getLegalIdFromEnterprisePartyId() {
		when(mockPartyClient.getLegalId(MUNICIPALITY_ID, PartyType.ENTERPRISE, "abc123")).thenReturn(Optional.of("1234"));
		String legalIdFromPartyId = partyIntegration.getLegalIdFromPartyId(MUNICIPALITY_ID, "abc123", "orgnr");
		assertThat(legalIdFromPartyId).isEqualTo("1234");
	}

	@Test
	void getLegalIdFromPrivatePartyId() {
		when(mockPartyClient.getLegalId(MUNICIPALITY_ID, PartyType.PRIVATE, "1234")).thenReturn(Optional.of("abc123"));
		String legalIdFromPartyId = partyIntegration.getLegalIdFromPartyId(MUNICIPALITY_ID, "1234", "pnr");
		assertThat(legalIdFromPartyId).isEqualTo("abc123");
	}

	@Test
	void getLegalIdFromPrivatePartyIdShouldThrowExceptionWhenNotFound() {
		when(mockPartyClient.getLegalId(MUNICIPALITY_ID, PartyType.ENTERPRISE, "1234")).thenReturn(Optional.empty());

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> partyIntegration.getLegalIdFromPartyId(MUNICIPALITY_ID, "1234", "orgnr"))
			.satisfies(problem -> {
				assertThat(problem.getTitle()).isEqualTo("Couldn't find any organization number / person for partyId: 1234");
				assertThat(problem.getStatus()).isEqualTo(Status.NOT_FOUND);
			});
	}
}
