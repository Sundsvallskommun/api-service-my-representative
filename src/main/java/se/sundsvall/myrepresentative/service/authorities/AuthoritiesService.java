package se.sundsvall.myrepresentative.service.authorities;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesRequest;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesResponse;
import se.sundsvall.myrepresentative.api.model.authorities.Authority;
import se.sundsvall.myrepresentative.integration.minaombud.ombud.OmbudIntegration;
import se.sundsvall.myrepresentative.integration.party.PartyClient;
import se.sundsvall.myrepresentative.service.jwt.JwtService;

import generated.se.sundsvall.minaombud.HamtaFullmakterRequest;
import generated.se.sundsvall.minaombud.HamtaFullmakterResponse;

@Service
public class AuthoritiesService {

    private final PartyClient partyClient;
    private final OmbudIntegration ombudIntegration;
    private final AuthoritiesRequestMapper authoritiesRequestMapper;
    private final AuthoritiesResponseMapper authoritiesResponseMapper;
    private final JwtService jwtService;

    public AuthoritiesService(PartyClient partyClient, OmbudIntegration ombudIntegration, AuthoritiesRequestMapper authoritiesRequestMapper, AuthoritiesResponseMapper authoritiesResponseMapper, JwtService jwtService) {
        this.partyClient = partyClient;
        this.ombudIntegration = ombudIntegration;
        this.authoritiesRequestMapper = authoritiesRequestMapper;
        this.authoritiesResponseMapper = authoritiesResponseMapper;
        this.jwtService = jwtService;
    }

    public AuthoritiesResponse getAuthorities(AuthoritiesRequest request) {
        Optional.ofNullable(request.getAuthorityIssuer())
                .ifPresent(issuer -> issuer.setLegalId(partyClient.getLegalIdFromPartyId(issuer.getPartyId(), issuer.getType())));
        request.getAuthorityAcquirer().setLegalId(partyClient.getLegalIdFromPartyId(request.getAuthorityAcquirer().getPartyId(), request.getAuthorityAcquirer().getType()));

        String xIdTokenHeader = jwtService.createSignedJwt(request.getAuthorityAcquirer().getLegalId());

        HamtaFullmakterRequest fullmakterRequest = authoritiesRequestMapper.createFullmakterRequest(request);
        HamtaFullmakterResponse fullmakterResponse = ombudIntegration.getFullmakter(xIdTokenHeader, fullmakterRequest);

        AuthoritiesResponse authoritiesResponse = authoritiesResponseMapper.mapFullmakterResponse(fullmakterResponse);

        mapLegalIdsToPartyIds(authoritiesResponse);

        return authoritiesResponse;
    }

    void mapLegalIdsToPartyIds(AuthoritiesResponse mandatesResponse) {
        mapLegalIdToPartyForIssuer(mandatesResponse.getAuthorities());
        mapLegalIdToPartyForAcquirers(mandatesResponse.getAuthorities());
    }

    void mapLegalIdToPartyForIssuer(List<Authority> mandates) {
        mandates.forEach(authority -> {
            String partyId = partyClient.getPartyIdFromLegalId(authority.getAuthorityIssuer().getLegalId(), authority.getAuthorityIssuer().getType());
            authority.getAuthorityIssuer().setPartyId(partyId);
        });
    }

    void mapLegalIdToPartyForAcquirers(List<Authority> mandates) {
        mandates.forEach(authority ->
                authority.getAuthorityAcquirers().forEach(acquirer ->
                        acquirer.setPartyId(partyClient.getPartyIdFromLegalId(acquirer.getLegalId(), acquirer.getType()))));
    }
}
