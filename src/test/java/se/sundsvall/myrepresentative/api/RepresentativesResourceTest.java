package se.sundsvall.myrepresentative.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import se.sundsvall.myrepresentative.TestObjectFactory;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesRequest;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesResponse;
import se.sundsvall.myrepresentative.api.model.jwks.Jwks;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesRequest;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesResponse;
import se.sundsvall.myrepresentative.api.validation.RequestValidator;
import se.sundsvall.myrepresentative.service.RepresentativesService;
import se.sundsvall.myrepresentative.service.jwt.JwtService;

@ExtendWith(MockitoExtension.class)
class RepresentativesResourceTest {

    @Mock
    private RepresentativesService mockService;

    @Mock
    private JwtService mockJwtService;

    @Mock
    private RequestValidator mockValidator;

    @InjectMocks
    private RepresentativesResource representativesResource;

    @Test
    void getMandates() {
        when(mockService.getMandates(any(MandatesRequest.class))).thenReturn(new MandatesResponse());
        Mockito.doNothing().when(mockValidator).validate(any(MandatesRequest.class));

        assertNotNull(representativesResource.getMandates(TestObjectFactory.createMandatesRequest()));

        verify(mockJwtService, times(0)).getJwks();
        verify(mockService, times(1)).getMandates(any(MandatesRequest.class));
    }

    @Test
    void jwks() {
        when(mockJwtService.getJwks()).thenReturn(new Jwks());

        assertNotNull(representativesResource.jwks());

        verify(mockJwtService, times(1)).getJwks();
        verify(mockService, times(0)).getMandates(any(MandatesRequest.class));
    }

    @Test
    void getAuthorities() {
        when(mockService.getAuthorities(any(AuthoritiesRequest.class))).thenReturn(new AuthoritiesResponse());
        Mockito.doNothing().when(mockValidator).validate(any(AuthoritiesRequest.class));

        assertNotNull(representativesResource.getAuthorities(TestObjectFactory.createAuthorityRequest()));

        verify(mockJwtService, times(0)).getJwks();
        verify(mockService, times(1)).getAuthorities(any(AuthoritiesRequest.class));
    }
}