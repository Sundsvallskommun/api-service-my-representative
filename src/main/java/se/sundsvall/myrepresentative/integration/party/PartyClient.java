package se.sundsvall.myrepresentative.integration.party;

import static generated.se.sundsvall.party.PartyType.ENTERPRISE;
import static generated.se.sundsvall.party.PartyType.PRIVATE;
import static org.zalando.problem.Status.NOT_FOUND;

import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;

import generated.se.sundsvall.party.PartyType;

@Component
public class PartyClient {

	private final PartyIntegration partyIntegration;

	public PartyClient(PartyIntegration partyIntegration) {
		this.partyIntegration = partyIntegration;
	}

	/**
	 * Fetch partyId for a private person or organization.
	 * @param legalId
	 * @return partyId or empty string if not found.
	 */
	public String getPartyIdFromLegalId(String legalId, String type) {
		return partyIntegration.getPartyId(getPartyTypeFromRequest(type), legalId)
				.orElse("");
	}

	/**
	 * Fetch legal id for an organization.
	 * @param partyId
	 * @return
	 */
	public String getLegalIdFromPartyId(String partyId, String type) {
		return partyIntegration.getLegalId(getPartyTypeFromRequest(type), partyId)
				.orElseThrow(() -> Problem.builder()
						.withTitle("Couldn't find any organization number / person for partyId: " + partyId)
						.withStatus(NOT_FOUND)
						.build()
				);
	}

	private PartyType getPartyTypeFromRequest(String type) {
		return switch (type) {
			case "pnr" -> PRIVATE;
			case "orgnr" -> ENTERPRISE;
			default -> throw new IllegalArgumentException("Unknown party type: " + type);
		};
	}
}
