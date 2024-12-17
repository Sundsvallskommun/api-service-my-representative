package se.sundsvall.myrepresentative.integration.party;

import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static se.sundsvall.myrepresentative.integration.party.PartyConfiguration.CLIENT_ID;

import generated.se.sundsvall.party.PartyType;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
	name = CLIENT_ID,
	url = "${integration.party.url}",
	configuration = PartyConfiguration.class,
	dismiss404 = true)
@CircuitBreaker(name = CLIENT_ID)
public interface PartyClient {

	/**
	 * Get legalID by partyId
	 *
	 * @param  partyType "ENERPRISE" or "PRIVATE"
	 * @param  partyId   uuid of the person or organization
	 * @return           legalId of the person or organization, Optional.empty() if not found.
	 */
	@Cacheable("legalIds")
	@GetMapping(path = "/{municipalityId}/{type}/{partyId}/legalId", produces = {
		TEXT_PLAIN_VALUE, APPLICATION_PROBLEM_JSON_VALUE
	})
	Optional<String> getLegalId(@PathVariable("municipalityId") String municipalityId, @PathVariable("type") PartyType partyType, @PathVariable("partyId") String partyId);

	/**
	 * Get partyId for a legalId
	 *
	 * @param  partyType "ENTERPRISE" or "PRIVATE"
	 * @param  legalId   legalid of the person or organization
	 * @return           partyId of the person or organization, Optional.empty() if not found.
	 */
	@Cacheable("partyIds")
	@GetMapping(path = "/{municipalityId}/{type}/{legalId}/partyId", produces = {
		TEXT_PLAIN_VALUE, APPLICATION_PROBLEM_JSON_VALUE
	})
	Optional<String> getPartyId(@PathVariable("municipalityId") String municipalityId, @PathVariable("type") PartyType partyType, @PathVariable("legalId") String legalId);
}
