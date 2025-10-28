package se.sundsvall.myrepresentative.integration.party;

import static se.sundsvall.myrepresentative.integration.party.configuration.PartyConfiguration.CLIENT_ID;

import generated.se.sundsvall.party.PartyType;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.sundsvall.myrepresentative.integration.party.configuration.PartyConfiguration;

@FeignClient(name = CLIENT_ID,
	url = "${integration.party.url}",
	configuration = PartyConfiguration.class,
	dismiss404 = true)
@CircuitBreaker(name = CLIENT_ID)
public interface PartyClient {

	@GetMapping("/{municipalityId}/{type}/{partyId}/legalId")
	Optional<String> getLegalIdByPartyId(
		@PathVariable(name = "municipalityId") final String municipalityId,
		@PathVariable(name = "type") final PartyType type,
		@PathVariable(name = "partyId") final String partyId);

}
