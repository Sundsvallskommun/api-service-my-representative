package se.sundsvall.myrepresentative.service.authorities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import generated.se.sundsvall.minaombud.HamtaFullmakterRequest;
import generated.se.sundsvall.minaombud.HamtaFullmakterResponse;
import se.sundsvall.myrepresentative.TestObjectFactory;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesRequest;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesResponse;
import se.sundsvall.myrepresentative.integration.minaombud.ombud.OmbudIntegration;
import se.sundsvall.myrepresentative.integration.party.PartyClient;
import se.sundsvall.myrepresentative.service.jwt.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthoritiesServiceTest {

	@Mock
	private PartyClient mockPartyClient;

	@Mock
	private OmbudIntegration mockOmbudIntegration;

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

        when(mockPartyClient.getLegalIdFromPartyId("acquirerPartyId", "orgnr")).thenReturn("1234567890");
        when(mockPartyClient.getLegalIdFromPartyId("issuerPartyId", "orgnr")).thenReturn("0987654321");

        when(mockPartyClient.getPartyIdFromLegalId("1234567890", "orgnr")).thenReturn("acquirerPartyId");
        when(mockPartyClient.getPartyIdFromLegalId("0987654321", "orgnr")).thenReturn("issuerPartyId");

        when(mockJwtService.createSignedJwt("1234567890")).thenReturn("xIdTokenHeader");
        when(mockRequestMapper.createFullmakterRequest(any(AuthoritiesRequest.class))).thenReturn(new HamtaFullmakterRequest());
        when(mockOmbudIntegration.getFullmakter(anyString(), any(HamtaFullmakterRequest.class))).thenReturn(new HamtaFullmakterResponse());
        when(mockResponseMapper.mapFullmakterResponse(any(HamtaFullmakterResponse.class))).thenReturn(TestObjectFactory.createAuthorityResponse());

        final AuthoritiesResponse authoritiesResponse = authoritiesService.getAuthorities(TestObjectFactory.createAuthorityRequest());

        assertThat(authoritiesResponse).isNotNull();

        // Only verify behavior, mappers etc are tested separately.
        verify(mockPartyClient, times(1)).getLegalIdFromPartyId("acquirerPartyId", "orgnr");
        verify(mockPartyClient, times(1)).getLegalIdFromPartyId("issuerPartyId", "orgnr");
        verify(mockPartyClient, times(1)).getPartyIdFromLegalId("1234567890", "orgnr");
        verify(mockPartyClient, times(1)).getPartyIdFromLegalId("0987654321", "orgnr");

        verify(mockJwtService, times(1)).createSignedJwt("1234567890");
        verify(mockRequestMapper, times(1)).createFullmakterRequest(any(AuthoritiesRequest.class));
        verify(mockOmbudIntegration, times(1)).getFullmakter(eq("xIdTokenHeader"), any(HamtaFullmakterRequest.class));
        verify(mockResponseMapper, times(1)).mapFullmakterResponse(any(HamtaFullmakterResponse.class));
    }
}
