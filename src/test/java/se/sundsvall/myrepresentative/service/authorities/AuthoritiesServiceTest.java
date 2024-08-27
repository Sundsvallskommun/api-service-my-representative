package se.sundsvall.myrepresentative.service.authorities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import se.sundsvall.myrepresentative.TestObjectFactory;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesRequest;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesResponse;
import se.sundsvall.myrepresentative.integration.minaombud.ombud.OmbudClient;
import se.sundsvall.myrepresentative.integration.party.PartyIntegration;
import se.sundsvall.myrepresentative.service.jwt.JwtService;

import generated.se.sundsvall.minaombud.HamtaFullmakterRequest;
import generated.se.sundsvall.minaombud.HamtaFullmakterResponse;

@ExtendWith(MockitoExtension.class)
class AuthoritiesServiceTest {

	@Mock
	private PartyIntegration mockPartyIntegration;

	@Mock
	private OmbudClient mockOmbudClient;

	@Mock
	private AuthoritiesRequestMapper mockRequestMapper;

	@Mock
	private AuthoritiesResponseMapper mockResponseMapper;

	@Mock
	private JwtService mockJwtService;

	@InjectMocks
	private AuthoritiesService authoritiesService;

	@Test
	void getAuthorities() {

		when(mockPartyIntegration.getLegalIdFromPartyId(MUNICIPALITY_ID, "acquirerPartyId", "orgnr")).thenReturn("1234567890");
		when(mockPartyIntegration.getLegalIdFromPartyId(MUNICIPALITY_ID, "issuerPartyId", "orgnr")).thenReturn("0987654321");

		when(mockPartyIntegration.getPartyIdFromLegalId(MUNICIPALITY_ID, "1234567890", "orgnr")).thenReturn("acquirerPartyId");
		when(mockPartyIntegration.getPartyIdFromLegalId(MUNICIPALITY_ID, "0987654321", "orgnr")).thenReturn("issuerPartyId");

		when(mockJwtService.createSignedJwt("1234567890")).thenReturn("xIdTokenHeader");
		when(mockRequestMapper.createFullmakterRequest(any(AuthoritiesRequest.class))).thenReturn(new HamtaFullmakterRequest());
		when(mockOmbudClient.getFullmakter(anyString(), any(HamtaFullmakterRequest.class))).thenReturn(new HamtaFullmakterResponse());
		when(mockResponseMapper.mapFullmakterResponse(any(HamtaFullmakterResponse.class))).thenReturn(TestObjectFactory.createAuthorityResponse());

		final AuthoritiesResponse authoritiesResponse = authoritiesService.getAuthorities(MUNICIPALITY_ID, TestObjectFactory.createAuthorityRequest());

		assertThat(authoritiesResponse).isNotNull();

		// Only verify behavior, mappers etc are tested separately.
		verify(mockPartyIntegration, times(1)).getLegalIdFromPartyId(MUNICIPALITY_ID, "acquirerPartyId", "orgnr");
		verify(mockPartyIntegration, times(1)).getLegalIdFromPartyId(MUNICIPALITY_ID, "issuerPartyId", "orgnr");
		verify(mockPartyIntegration, times(1)).getPartyIdFromLegalId(MUNICIPALITY_ID, "1234567890", "orgnr");
		verify(mockPartyIntegration, times(1)).getPartyIdFromLegalId(MUNICIPALITY_ID, "0987654321", "orgnr");

		verify(mockJwtService, times(1)).createSignedJwt("1234567890");
		verify(mockRequestMapper, times(1)).createFullmakterRequest(any(AuthoritiesRequest.class));
		verify(mockOmbudClient, times(1)).getFullmakter(eq("xIdTokenHeader"), any(HamtaFullmakterRequest.class));
		verify(mockResponseMapper, times(1)).mapFullmakterResponse(any(HamtaFullmakterResponse.class));
	}
}
