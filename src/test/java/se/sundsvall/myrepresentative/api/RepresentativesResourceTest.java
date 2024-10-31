package se.sundsvall.myrepresentative.api;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;

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
		when(mockService.getMandates(eq(MUNICIPALITY_ID), any(MandatesRequest.class))).thenReturn(new MandatesResponse());
		Mockito.doNothing().when(mockValidator).validate(any(MandatesRequest.class));

		assertNotNull(representativesResource.getMandates(MUNICIPALITY_ID, TestObjectFactory.createMandatesRequest()));

		verify(mockJwtService, never()).getJwks(MUNICIPALITY_ID);
		verify(mockService).getMandates(eq(MUNICIPALITY_ID), any(MandatesRequest.class));
	}

	@Test
	void jwks() {
		when(mockJwtService.getJwks(MUNICIPALITY_ID)).thenReturn(new Jwks());

		assertNotNull(representativesResource.jwks(MUNICIPALITY_ID));

		verify(mockJwtService).getJwks(MUNICIPALITY_ID);
		verify(mockService, never()).getMandates(eq(MUNICIPALITY_ID), any(MandatesRequest.class));
	}

	@Test
	void getAuthorities() {
		when(mockService.getAuthorities(eq(MUNICIPALITY_ID), any(AuthoritiesRequest.class))).thenReturn(new AuthoritiesResponse());
		Mockito.doNothing().when(mockValidator).validate(any(AuthoritiesRequest.class));

		assertNotNull(representativesResource.getAuthorities(MUNICIPALITY_ID, TestObjectFactory.createAuthorityRequest()));

		verify(mockJwtService, never()).getJwks(MUNICIPALITY_ID);
		verify(mockService).getAuthorities(eq(MUNICIPALITY_ID), any(AuthoritiesRequest.class));
	}
}
