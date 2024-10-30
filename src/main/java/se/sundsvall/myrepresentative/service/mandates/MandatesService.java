package se.sundsvall.myrepresentative.service.mandates;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import se.sundsvall.myrepresentative.api.model.mandates.Mandate;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesRequest;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesResponse;
import se.sundsvall.myrepresentative.integration.minaombud.ombud.OmbudClient;
import se.sundsvall.myrepresentative.integration.party.PartyIntegration;
import se.sundsvall.myrepresentative.service.jwt.JwtService;
import se.sundsvall.myrepresentative.service.signature.SignatureVerificator;

import generated.se.sundsvall.minaombud.HamtaBehorigheterRequest;
import generated.se.sundsvall.minaombud.HamtaBehorigheterResponse;

@Service
public class MandatesService {

	private final JwtService jwtService;
	private final OmbudClient ombudClient;
	private final PartyIntegration partyIntegration;
	private final MandatesRequestMapper mandatesRequestMapper;
	private final MandatesResponseMapper mandatesResponseMapper;
	private final SignatureVerificator signatureVerificator;

	public MandatesService(JwtService jwtService, OmbudClient ombudClient, MandatesResponseMapper mandatesResponseMapper, MandatesRequestMapper mandatesRequestMapper, PartyIntegration partyIntegration, SignatureVerificator signatureVerificator) {
		this.jwtService = jwtService;
		this.ombudClient = ombudClient;
		this.mandatesResponseMapper = mandatesResponseMapper;
		this.mandatesRequestMapper = mandatesRequestMapper;
		this.partyIntegration = partyIntegration;
		this.signatureVerificator = signatureVerificator;
	}

	public MandatesResponse getMandates(final String municipalityId, final MandatesRequest mandatesRequest) {
		// Set legalId of the issuer and acquirer
		Optional.ofNullable(mandatesRequest.getMandateIssuer())
			.ifPresent(issuer -> issuer.setLegalId(partyIntegration.getLegalIdFromPartyId(municipalityId, issuer.getPartyId(), issuer.getType())));
		mandatesRequest.getMandateAcquirer().setLegalId(partyIntegration.getLegalIdFromPartyId(municipalityId, mandatesRequest.getMandateAcquirer().getPartyId(), mandatesRequest.getMandateAcquirer().getType()));

		String xIdTokenHeader = jwtService.createSignedJwt(mandatesRequest.getMandateAcquirer().getLegalId());

		HamtaBehorigheterRequest behorigheterRequest = mandatesRequestMapper.createBehorigheterRequest(mandatesRequest);
		HamtaBehorigheterResponse behorigheterResponse = ombudClient.getBehorigheter(xIdTokenHeader, behorigheterRequest);

		// Validate the signature from bolagsverket, should throw exception if we cannot validate the signature.
		MandatesResponse mandatesResponse = validateAndMapResponseFromBolagsverket(municipalityId, behorigheterResponse);

		// Populate the response with legalIds
		mapLegalIdsToPartyIds(municipalityId, mandatesResponse);

		return mandatesResponse;
	}

	void mapLegalIdsToPartyIds(final String municipalityId, final MandatesResponse mandatesResponse) {
		mapLegalIdToPartyForIssuer(municipalityId, mandatesResponse.getMandates());
		mapLegalIdToPartyForAcquirers(municipalityId, mandatesResponse.getMandates());
	}

	void mapLegalIdToPartyForIssuer(final String municipalityId, final List<Mandate> mandates) {
		mandates.forEach(mandate -> {
			String partyId = partyIntegration.getPartyIdFromLegalId(municipalityId, mandate.getMandateIssuer().getLegalId(), mandate.getMandateIssuer().getType());
			mandate.getMandateIssuer().setPartyId(partyId);
		});
	}

	void mapLegalIdToPartyForAcquirers(final String municipalityId, final List<Mandate> mandates) {
		mandates.forEach(mandate -> mandate.getMandateAcquirers().forEach(acquirer -> acquirer.setPartyId(partyIntegration.getPartyIdFromLegalId(municipalityId, acquirer.getLegalId(), acquirer.getType()))));
	}

	MandatesResponse validateAndMapResponseFromBolagsverket(final String municipalityId, final HamtaBehorigheterResponse behorigheterResponse) {
		if (!behorigheterResponse.getKontext().isEmpty()) {
			signatureVerificator.verifySignatures(behorigheterResponse);
		}
		return mandatesResponseMapper.mapFullmakterResponse(municipalityId, behorigheterResponse);
	}
}
