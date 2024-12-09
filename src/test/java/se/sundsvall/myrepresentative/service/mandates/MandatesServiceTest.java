package se.sundsvall.myrepresentative.service.mandates;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;

import generated.se.sundsvall.minaombud.HamtaBehorigheterRequest;
import generated.se.sundsvall.minaombud.HamtaBehorigheterResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.myrepresentative.TestObjectFactory;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesRequest;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesResponse;
import se.sundsvall.myrepresentative.integration.minaombud.ombud.OmbudClient;
import se.sundsvall.myrepresentative.integration.party.PartyIntegration;
import se.sundsvall.myrepresentative.service.jwt.JwtService;

@ExtendWith(MockitoExtension.class)
class MandatesServiceTest {

	@Mock
	private OmbudClient mockOmbudClient;
	@Mock
	private PartyIntegration mockPartyIntegration;
	@Mock
	private MandatesResponseMapper mockMandatesResponseMapper;
	@Mock
	private MandatesRequestMapper mockMandatesRequestMapper;
	@Mock
	private JwtService mockJwtService;

	@InjectMocks
	private MandatesService mandatesService;

	@Test
	void testGetPermissions() {
		when(mockPartyIntegration.getLegalIdFromPartyId(MUNICIPALITY_ID, "acquirerPartyId", "orgnr")).thenReturn("1234567890");
		when(mockPartyIntegration.getLegalIdFromPartyId(MUNICIPALITY_ID, "issuerPartyId", "orgnr")).thenReturn("0987654321");

		when(mockPartyIntegration.getPartyIdFromLegalId(MUNICIPALITY_ID, "1234567890", "orgnr")).thenReturn("acquirerPartyId");
		when(mockPartyIntegration.getPartyIdFromLegalId(MUNICIPALITY_ID, "0987654321", "orgnr")).thenReturn("issuerPartyId");

		when(mockJwtService.createSignedJwt("1234567890")).thenReturn("xIdTokenHeader");
		when(mockMandatesRequestMapper.createBehorigheterRequest(any(MandatesRequest.class))).thenReturn(new HamtaBehorigheterRequest());
		when(mockOmbudClient.getBehorigheter(anyString(), any(HamtaBehorigheterRequest.class))).thenReturn(new HamtaBehorigheterResponse());
		when(mockMandatesResponseMapper.mapFullmakterResponse(eq(MUNICIPALITY_ID), any(HamtaBehorigheterResponse.class))).thenReturn(TestObjectFactory.createMandatesResponse());

		final MandatesResponse permissions = mandatesService.getMandates(MUNICIPALITY_ID, TestObjectFactory.createMandatesRequest());

		assertThat(permissions).isNotNull();

		// Only verify behavior, mappers etc are tested separately.
		verify(mockPartyIntegration).getLegalIdFromPartyId(MUNICIPALITY_ID, "acquirerPartyId", "orgnr");
		verify(mockPartyIntegration).getLegalIdFromPartyId(MUNICIPALITY_ID, "issuerPartyId", "orgnr");
		verify(mockPartyIntegration).getPartyIdFromLegalId(MUNICIPALITY_ID, "1234567890", "orgnr");
		verify(mockPartyIntegration).getPartyIdFromLegalId(MUNICIPALITY_ID, "0987654321", "orgnr");

		verify(mockJwtService).createSignedJwt("1234567890");
		verify(mockMandatesRequestMapper).createBehorigheterRequest(any(MandatesRequest.class));
		verify(mockOmbudClient).getBehorigheter(eq("xIdTokenHeader"), any(HamtaBehorigheterRequest.class));
		verify(mockMandatesResponseMapper).mapFullmakterResponse(eq(MUNICIPALITY_ID), any(HamtaBehorigheterResponse.class));
	}
}
