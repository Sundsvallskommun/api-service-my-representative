package se.sundsvall.myrepresentative.integration.legalentity;

import static org.zalando.problem.Status.NOT_FOUND;

import generated.se.sundsvall.legalentity.PersonEngagement;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;

@Component
public class LegalEntityIntegration {

	private final LegalEntityClient legalEntityClient;

	public LegalEntityIntegration(final LegalEntityClient legalEntityClient) {
		this.legalEntityClient = legalEntityClient;
	}

	public List<PersonEngagement> getPersonEngagements(final String municipalityId, final String personNumber) {
		final var engagements = legalEntityClient.getPersonEngagements(municipalityId, personNumber);

		if (CollectionUtils.isEmpty(engagements)) {
			throw Problem.builder()
				.withTitle("No engagements found")
				.withStatus(NOT_FOUND)
				.withDetail("No matching engagements found for the given person")
				.build();
		}

		return engagements;
	}
}
