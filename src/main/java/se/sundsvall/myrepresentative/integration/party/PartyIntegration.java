package se.sundsvall.myrepresentative.integration.party;

import java.util.Optional;
import org.springframework.stereotype.Component;

import static generated.se.sundsvall.party.PartyType.ENTERPRISE;
import static generated.se.sundsvall.party.PartyType.PRIVATE;

@Component
public class PartyIntegration {

	private final PartyClient partyClient;

	public PartyIntegration(final PartyClient partyClient) {
		this.partyClient = partyClient;
	}

	/**
	 * Retrieves the legal ID for an organization for the given municipalityId and partyId.
	 *
	 * @param  municipalityId municipalityId
	 * @param  partyId        partyId
	 * @return                the legal ID if found, otherwise an empty Optional
	 */
	public Optional<String> getOrganizationLegalId(final String municipalityId, final String partyId) {
		return partyClient.getLegalIdByPartyId(municipalityId, ENTERPRISE, partyId);
	}

	/**
	 * Retrieves the legal ID for a person for the given municipalityId and partyId.
	 *
	 * @param  municipalityId municipalityId
	 * @param  partyId        partyId
	 * @return                the legal ID if found, otherwise an empty Optional
	 */
	public Optional<String> getPersonalLegalId(final String municipalityId, final String partyId) {
		return partyClient.getLegalIdByPartyId(municipalityId, PRIVATE, partyId);
	}
}
