package se.sundsvall.myrepresentative.integration.party;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.Status;
import org.zalando.problem.ThrowableProblem;

import generated.se.sundsvall.party.PartyType;

@ExtendWith(MockitoExtension.class)
class PartyClientTest {

    @Mock
    private PartyIntegration mockPartyIntegration;

    @InjectMocks
    private PartyClient partyClient;

    @Test
    void getPartyIdFromEnterpriseLegalId() {
        when(mockPartyIntegration.getPartyId(PartyType.ENTERPRISE, "1234")).thenReturn(Optional.of("abc123"));
        String partyIdFromLegalId = partyClient.getPartyIdFromLegalId("1234", "orgnr");
        assertThat(partyIdFromLegalId).isEqualTo("abc123");
    }

    @Test
    void getPartyIdFromPrivateLegalId() {
        when(mockPartyIntegration.getPartyId(PartyType.PRIVATE, "1234")).thenReturn(Optional.of("abc123"));
        String partyIdFromLegalId = partyClient.getPartyIdFromLegalId("1234", "pnr");
        assertThat(partyIdFromLegalId).isEqualTo("abc123");
    }

    @Test
    void getLegalIdFromEnterprisePartyId() {
        when(mockPartyIntegration.getLegalId(PartyType.ENTERPRISE, "abc123")).thenReturn(Optional.of("1234"));
        String legalIdFromPartyId = partyClient.getLegalIdFromPartyId("abc123", "orgnr");
        assertThat(legalIdFromPartyId).isEqualTo("1234");
    }

    @Test
    void getLegalIdFromPrivatePartyId() {
        when(mockPartyIntegration.getLegalId(PartyType.PRIVATE, "1234")).thenReturn(Optional.of("abc123"));
        String legalIdFromPartyId = partyClient.getLegalIdFromPartyId("1234", "pnr");
        assertThat(legalIdFromPartyId).isEqualTo("abc123");
    }

    @Test
    void getLegalIdFromPrivatePartyId_shouldThrowException_whenNotFound() {
        when(mockPartyIntegration.getLegalId(PartyType.ENTERPRISE, "1234")).thenReturn(Optional.empty());

        assertThatExceptionOfType(ThrowableProblem.class)
                .isThrownBy(() -> partyClient.getLegalIdFromPartyId("1234", "orgnr"))
                .satisfies(problem -> {
                    assertThat(problem.getTitle()).isEqualTo("Couldn't find any organization number / person for partyId: 1234");
                    assertThat(problem.getStatus()).isEqualTo(Status.NOT_FOUND);
                });
    }
}