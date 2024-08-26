package se.sundsvall.myrepresentative.service;

import org.springframework.stereotype.Service;

import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesRequest;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesResponse;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesRequest;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesResponse;
import se.sundsvall.myrepresentative.service.authorities.AuthoritiesService;
import se.sundsvall.myrepresentative.service.mandates.MandatesService;

/**
 * Class for mainly separating concerns between authorities and mandates.
 */
@Service
public class RepresentativesService {

	private final AuthoritiesService authoritiesService;
	private final MandatesService mandatesService;

	public RepresentativesService(AuthoritiesService authoritiesService, MandatesService mandatesService) {
		this.authoritiesService = authoritiesService;
		this.mandatesService = mandatesService;
	}

	public MandatesResponse getMandates(final String municipalityId, final MandatesRequest mandatesRequest) {
		return mandatesService.getMandates(municipalityId, mandatesRequest);
	}

	public AuthoritiesResponse getAuthorities(final String municipalityId, final AuthoritiesRequest request) {
		return authoritiesService.getAuthorities(municipalityId, request);
	}
}
