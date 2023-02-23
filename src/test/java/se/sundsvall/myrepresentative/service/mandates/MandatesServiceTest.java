package se.sundsvall.myrepresentative.service.mandates;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nimbusds.jose.JOSEException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import se.sundsvall.myrepresentative.TestObjectFactory;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesRequest;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesResponse;
import se.sundsvall.myrepresentative.integration.minaombud.ombud.OmbudIntegration;
import se.sundsvall.myrepresentative.integration.party.PartyClient;
import se.sundsvall.myrepresentative.service.jwt.JwtService;

import generated.se.sundsvall.minaombud.HamtaBehorigheterRequest;
import generated.se.sundsvall.minaombud.HamtaBehorigheterResponse;

@ExtendWith(MockitoExtension.class)
class MandatesServiceTest {

    @Mock
    private OmbudIntegration mockOmbudIntegration;
    @Mock
    private PartyClient mockPartyClient;
    @Mock
    private MandatesResponseMapper mockMandatesResponseMapper;
    @Mock
    private MandatesRequestMapper mockMandatesRequestMapper;
    @Mock
    private JwtService mockJwtService;

    @InjectMocks
    private MandatesService mandatesService;

    @Test
    void testGetPermissions() throws JOSEException {
        when(mockPartyClient.getLegalIdFromPartyId("acquirerPartyId", "orgnr")).thenReturn("1234567890");
        when(mockPartyClient.getLegalIdFromPartyId("issuerPartyId", "orgnr")).thenReturn("0987654321");

        when(mockPartyClient.getPartyIdFromLegalId("1234567890", "orgnr")).thenReturn("acquirerPartyId");
        when(mockPartyClient.getPartyIdFromLegalId("0987654321", "orgnr")).thenReturn("issuerPartyId");

        when(mockJwtService.createSignedJwt("1234567890")).thenReturn("xIdTokenHeader");
        when(mockMandatesRequestMapper.createBehorigheterRequest(any(MandatesRequest.class))).thenReturn(new HamtaBehorigheterRequest());
        when(mockOmbudIntegration.getBehorigheter(anyString(), any(HamtaBehorigheterRequest.class))).thenReturn(new HamtaBehorigheterResponse());
        when(mockMandatesResponseMapper.mapFullmakterResponse(any(HamtaBehorigheterResponse.class))).thenReturn(TestObjectFactory.createMandatesResponse());

        MandatesResponse permissions = mandatesService.getMandates(TestObjectFactory.createMandatesRequest());

        assertThat(permissions).isNotNull();

        // Only verify behavior, mappers etc are tested separately.
        verify(mockPartyClient, times(1)).getLegalIdFromPartyId("acquirerPartyId", "orgnr");
        verify(mockPartyClient, times(1)).getLegalIdFromPartyId("issuerPartyId", "orgnr");
        verify(mockPartyClient, times(1)).getPartyIdFromLegalId("1234567890", "orgnr");
        verify(mockPartyClient, times(1)).getPartyIdFromLegalId("0987654321", "orgnr");

        verify(mockJwtService, times(1)).createSignedJwt("1234567890");
        verify(mockMandatesRequestMapper, times(1)).createBehorigheterRequest(any(MandatesRequest.class));
        verify(mockOmbudIntegration, times(1)).getBehorigheter(eq("xIdTokenHeader"), any(HamtaBehorigheterRequest.class));
        verify(mockMandatesResponseMapper, times(1)).mapFullmakterResponse(any(HamtaBehorigheterResponse.class));
    }
}