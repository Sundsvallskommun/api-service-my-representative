package se.sundsvall.myrepresentative.service.authorities;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesRequest;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesResponse;
import se.sundsvall.myrepresentative.api.model.authorities.Authority;
import se.sundsvall.myrepresentative.integration.minaombud.ombud.OmbudClient;
import se.sundsvall.myrepresentative.integration.party.PartyIntegration;
import se.sundsvall.myrepresentative.service.jwt.JwtService;

import generated.se.sundsvall.minaombud.HamtaFullmakterRequest;
import generated.se.sundsvall.minaombud.HamtaFullmakterResponse;

@Service
public class AuthoritiesService {

	private final PartyIntegration partyIntegration;
	private final OmbudClient ombudClient;
	private final AuthoritiesRequestMapper authoritiesRequestMapper;
	private final AuthoritiesResponseMapper authoritiesResponseMapper;
	private final JwtService jwtService;

	public AuthoritiesService(PartyIntegration partyIntegration, OmbudClient ombudClient, AuthoritiesRequestMapper authoritiesRequestMapper, AuthoritiesResponseMapper authoritiesResponseMapper, JwtService jwtService) {
		this.partyIntegration = partyIntegration;
		this.ombudClient = ombudClient;
		this.authoritiesRequestMapper = authoritiesRequestMapper;
		this.authoritiesResponseMapper = authoritiesResponseMapper;
		this.jwtService = jwtService;
	}

	public AuthoritiesResponse getAuthorities(final String municipalityId, final AuthoritiesRequest request) {
		Optional.ofNullable(request.getAuthorityIssuer())
			.ifPresent(issuer -> issuer.setLegalId(partyIntegration.getLegalIdFromPartyId(municipalityId, issuer.getPartyId(), issuer.getType())));
		request.getAuthorityAcquirer().setLegalId(partyIntegration.getLegalIdFromPartyId(municipalityId, request.getAuthorityAcquirer().getPartyId(), request.getAuthorityAcquirer().getType()));

		String xIdTokenHeader = jwtService.createSignedJwt(request.getAuthorityAcquirer().getLegalId());

		HamtaFullmakterRequest fullmakterRequest = authoritiesRequestMapper.createFullmakterRequest(request);
		HamtaFullmakterResponse fullmakterResponse = ombudClient.getFullmakter(xIdTokenHeader, fullmakterRequest);

		AuthoritiesResponse authoritiesResponse = authoritiesResponseMapper.mapFullmakterResponse(fullmakterResponse);

		mapLegalIdsToPartyIds(municipalityId, authoritiesResponse);

		return authoritiesResponse;
	}

	void mapLegalIdsToPartyIds(final String municipalityId, final AuthoritiesResponse mandatesResponse) {
		mapLegalIdToPartyForIssuer(municipalityId, mandatesResponse.getAuthorities());
		mapLegalIdToPartyForAcquirers(municipalityId, mandatesResponse.getAuthorities());
	}

	void mapLegalIdToPartyForIssuer(final String municipalityId, final List<Authority> mandates) {
		mandates.forEach(authority -> {
			String partyId = partyIntegration.getPartyIdFromLegalId(municipalityId, authority.getAuthorityIssuer().getLegalId(), authority.getAuthorityIssuer().getType());
			authority.getAuthorityIssuer().setPartyId(partyId);
		});
	}

	void mapLegalIdToPartyForAcquirers(final String municipalityId, final List<Authority> mandates) {
		mandates.forEach(authority -> authority.getAuthorityAcquirers().forEach(acquirer -> acquirer.setPartyId(partyIntegration.getPartyIdFromLegalId(municipalityId, acquirer.getLegalId(), acquirer.getType()))));
	}
}
