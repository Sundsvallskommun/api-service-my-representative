package se.sundsvall.myrepresentative.service.mandates;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import se.sundsvall.myrepresentative.api.model.mandates.Mandate;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesRequest;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesResponse;
import se.sundsvall.myrepresentative.integration.minaombud.ombud.OmbudIntegration;
import se.sundsvall.myrepresentative.integration.party.PartyClient;
import se.sundsvall.myrepresentative.service.jwt.JwtService;
import se.sundsvall.myrepresentative.service.signature.SignatureValidator;

import generated.se.sundsvall.minaombud.HamtaBehorigheterRequest;
import generated.se.sundsvall.minaombud.HamtaBehorigheterResponse;

@Service
public class MandatesService {

    private final JwtService jwtService;
    private final OmbudIntegration ombudIntegration;
    private final PartyClient partyClient;
    private final MandatesRequestMapper mandatesRequestMapper;
    private final MandatesResponseMapper mandatesResponseMapper;
    private final SignatureValidator signatureValidator;

    public MandatesService(JwtService jwtService, OmbudIntegration ombudIntegration, MandatesResponseMapper mandatesResponseMapper, MandatesRequestMapper mandatesRequestMapper, PartyClient partyClient, SignatureValidator signatureValidator) {
        this.jwtService = jwtService;
        this.ombudIntegration = ombudIntegration;
        this.mandatesResponseMapper = mandatesResponseMapper;
        this.mandatesRequestMapper = mandatesRequestMapper;
        this.partyClient = partyClient;
        this.signatureValidator = signatureValidator;
    }

    public MandatesResponse getMandates(MandatesRequest mandatesRequest) {
        //Set legalId of the issuer and acquirer
        Optional.ofNullable(mandatesRequest.getMandateIssuer())
                .ifPresent(issuer -> issuer.setLegalId(partyClient.getLegalIdFromPartyId(issuer.getPartyId(), issuer.getType())));
        mandatesRequest.getMandateAcquirer().setLegalId(partyClient.getLegalIdFromPartyId(mandatesRequest.getMandateAcquirer().getPartyId(), mandatesRequest.getMandateAcquirer().getType()));

        String xIdTokenHeader = jwtService.createSignedJwt(mandatesRequest.getMandateAcquirer().getLegalId());

        HamtaBehorigheterRequest behorigheterRequest = mandatesRequestMapper.createBehorigheterRequest(mandatesRequest);
        HamtaBehorigheterResponse behorigheterResponse = ombudIntegration.getBehorigheter(xIdTokenHeader, behorigheterRequest);

        //Validate the signature from bolagsverket, should throw exception if we cannot validate the signature.
        MandatesResponse mandatesResponse = validateAndMapResponseFromBolagsverket(behorigheterResponse);


        //Populate the response with legalIds
        mapLegalIdsToPartyIds(mandatesResponse);

        return mandatesResponse;
    }

    void mapLegalIdsToPartyIds(MandatesResponse mandatesResponse) {
        mapLegalIdToPartyForIssuer(mandatesResponse.getMandates());
        mapLegalIdToPartyForAcquirers(mandatesResponse.getMandates());
    }

    void mapLegalIdToPartyForIssuer(List<Mandate> mandates) {
        mandates.forEach(mandate -> {
            String partyId = partyClient.getPartyIdFromLegalId(mandate.getMandateIssuer().getLegalId(), mandate.getMandateIssuer().getType());
            mandate.getMandateIssuer().setPartyId(partyId);
        });
    }

    void mapLegalIdToPartyForAcquirers(List<Mandate> mandates) {
        mandates.forEach(mandate ->
                mandate.getMandateAcquirers().forEach(acquirer ->
                        acquirer.setPartyId(partyClient.getPartyIdFromLegalId(acquirer.getLegalId(), acquirer.getType()))));
    }

    MandatesResponse validateAndMapResponseFromBolagsverket(HamtaBehorigheterResponse behorigheterResponse) {
        if (!behorigheterResponse.getKontext().isEmpty()) {
            signatureValidator.validateSignatures(behorigheterResponse);
        }
        return mandatesResponseMapper.mapFullmakterResponse(behorigheterResponse);
    }
}
