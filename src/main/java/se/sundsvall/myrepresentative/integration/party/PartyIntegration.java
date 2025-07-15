package se.sundsvall.myrepresentative.integration.party;

import static generated.se.sundsvall.party.PartyType.ENTERPRISE;
import static generated.se.sundsvall.party.PartyType.PRIVATE;
import static org.zalando.problem.Status.NOT_FOUND;

import generated.se.sundsvall.party.PartyType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;

@Component
public class PartyIntegration {

	private final PartyClient partyClient;

	public PartyIntegration(PartyClient partyClient) {
		this.partyClient = partyClient;
	}

	/**
	 * Fetch partyId for a private person or organization.
	 *
	 * @param  legalId
	 * @return         partyId or empty string if not found.
	 */
	@Cacheable("partyIds")
	public String getPartyIdFromLegalId(String municipalityId, String legalId, String type) {
		return partyClient.getPartyId(municipalityId, getPartyTypeFromRequest(type), legalId)
			.orElse("");
	}

	/**
	 * Fetch legal id for an organization.
	 *
	 * @param  partyId
	 * @return
	 */
	@Cacheable("legalIds")
	public String getLegalIdFromPartyId(String municipalityId, String partyId, String type) {
		return partyClient.getLegalId(municipalityId, getPartyTypeFromRequest(type), partyId)
			.orElseThrow(() -> Problem.builder()
				.withTitle("Couldn't find any organization number / person for partyId: " + partyId)
				.withStatus(NOT_FOUND)
				.build());
	}

	private PartyType getPartyTypeFromRequest(String type) {
		return switch (type) {
			case "pnr" -> PRIVATE;
			case "orgnr" -> ENTERPRISE;
			default -> throw new IllegalArgumentException("Unknown party type: " + type);
		};
	}
}
