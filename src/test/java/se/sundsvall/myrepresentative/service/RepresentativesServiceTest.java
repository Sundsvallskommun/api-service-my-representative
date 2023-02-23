package se.sundsvall.myrepresentative.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesRequest;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesResponse;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesRequest;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesResponse;
import se.sundsvall.myrepresentative.service.authorities.AuthoritiesService;
import se.sundsvall.myrepresentative.service.mandates.MandatesService;

@ExtendWith(MockitoExtension.class)
class RepresentativesServiceTest {

    @Mock
    private AuthoritiesService mockAuthoritiesService;

    @Mock
    private MandatesService mockMandatesService;

    @InjectMocks
    private RepresentativesService representativesService;

    //Kinda useless tests but...

    @Test
    void testgetMandates() {
        when(mockMandatesService.getMandates(any(MandatesRequest.class))).thenReturn(new MandatesResponse());
        assertThat(representativesService.getMandates(new MandatesRequest())).isNotNull();
    }

    @Test
    void getAuthorities() {
        when(mockAuthoritiesService.getAuthorities(any(AuthoritiesRequest.class))).thenReturn(new AuthoritiesResponse());
        assertThat(representativesService.getAuthorities(new AuthoritiesRequest())).isNotNull();
    }

}