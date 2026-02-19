package se.sundsvall.myrepresentative.integration.legalentity;

import generated.se.sundsvall.legalentity.PersonEngagement;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import se.sundsvall.myrepresentative.integration.legalentity.configuration.LegalEntityConfiguration;

import static se.sundsvall.myrepresentative.integration.legalentity.configuration.LegalEntityConfiguration.CLIENT_ID;

@FeignClient(name = CLIENT_ID,
	url = "${integration.legalentity.url}",
	configuration = LegalEntityConfiguration.class,
	dismiss404 = true)
@CircuitBreaker(name = CLIENT_ID)
public interface LegalEntityClient {

	@GetMapping("/{municipalityId}/engagements/person/{personNumber}")
	List<PersonEngagement> getPersonEngagements(
		@PathVariable final String municipalityId,
		@PathVariable final String personNumber);
}
