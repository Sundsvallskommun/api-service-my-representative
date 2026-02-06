package se.sundsvall.myrepresentative.service;

import static org.zalando.problem.Status.BAD_REQUEST;
import static org.zalando.problem.Status.FORBIDDEN;
import static org.zalando.problem.Status.NOT_FOUND;

import generated.se.sundsvall.legalentity.PersonEngagement;
import jakarta.validation.Validator;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.SigningInfo;
import se.sundsvall.myrepresentative.integration.legalentity.LegalEntityIntegration;
import se.sundsvall.myrepresentative.integration.party.PartyIntegration;

@Service
public class LegalEntityService {

	private static final Logger LOG = LoggerFactory.getLogger(LegalEntityService.class);

	private final PartyIntegration partyIntegration;
	private final LegalEntityIntegration legalEntityIntegration;
	private final Validator validator;

	public LegalEntityService(final PartyIntegration partyIntegration, final LegalEntityIntegration legalEntityIntegration, final Validator validator) {
		this.partyIntegration = partyIntegration;
		this.legalEntityIntegration = legalEntityIntegration;
		this.validator = validator;
	}

	/**
	 * Validates that the person in the given request is an authorized signatory for the given organization (grantor).
	 *
	 * @param  municipalityId   the municipalityId
	 * @param  request          the mandate creation request containing the grantor and signatory details
	 * @throws ThrowableProblem with status 404 if no engagement found, or 403 if not an authorized signatory
	 */
	public void validateSignatory(final String municipalityId, final CreateMandate request) {
		final var signatoryPartyId = request.grantorDetails().signatoryPartyId();
		final var grantorPartyId = request.grantorDetails().grantorPartyId();

		validateSigningInfo(request.signingInfo());

		final var grantorLegalId = getLegalIdForPartyId(municipalityId, grantorPartyId, true);
		final var signatoryLegalId = getLegalIdForPartyId(municipalityId, signatoryPartyId, false);

		final var personEngagements = legalEntityIntegration.getPersonEngagements(municipalityId, signatoryLegalId);

		validateSignatoryForOrganization(grantorLegalId, personEngagements);
	}

	private void validateSignatoryForOrganization(final String grantorLegalId, final List<PersonEngagement> personEngagements) {
		// First check that we can find the organization in the person's engagements, if not throw a 404
		final var matchingEngagement = personEngagements.stream()
			.filter(engagement -> grantorLegalId.equals(engagement.getOrganizationNumber())) // Match the organization number
			.findFirst()
			.orElseThrow(() -> {
				LOG.warn("No engagement found for organization: {}", grantorLegalId);
				return Problem.builder()
					.withTitle("No engagement found for organization")
					.withDetail("The person does not have any engagements with the organization with legalId: " + grantorLegalId)
					.withStatus(NOT_FOUND)
					.build();
			});

		// If we find the organization, check that the signatory is authorized, or else throw a 403
		if (!Boolean.TRUE.equals(matchingEngagement.getIsAuthorizedSignatory())) {
			LOG.warn("Person is not an authorized signatory for organization: {}", grantorLegalId);
			throw Problem.builder()
				.withTitle("Signatory not authorized")
				.withStatus(FORBIDDEN)
				.withDetail("The person is not an authorized signatory for the organization with legalId: " + grantorLegalId)
				.build();
		}
	}

	/**
	 * Retrieves the legalId for the given partyId, depending on whether it's an organization or a person.
	 *
	 * @param  municipalityId the municipalityId
	 * @param  partyId        the partyId to retrieve the legalId for
	 * @param  isOrganization indicates whether the partyId belongs to an organization (true) or a person (false)
	 * @return                the legalId
	 */
	private String getLegalIdForPartyId(final String municipalityId, final String partyId, final boolean isOrganization) {
		if (isOrganization) {
			return partyIntegration.getOrganizationLegalId(municipalityId, partyId)
				.orElseThrow(() -> {
					LOG.warn("No organization found for partyId '{}'", partyId);
					return throwLegalIdNotFound(partyId, "Organization not found");
				});
		}

		return partyIntegration.getPersonalLegalId(municipalityId, partyId)
			.orElseThrow(() -> {
				LOG.warn("No person found for partyId '{}'", partyId);
				return throwLegalIdNotFound(partyId, "Person not found");
			});
	}

	private static ThrowableProblem throwLegalIdNotFound(final String partyId, final String title) {
		return Problem.builder()
			.withTitle(title)
			.withStatus(NOT_FOUND)
			.withDetail("No legalId found for partyId: " + partyId)
			.build();
	}

	private void validateSigningInfo(final SigningInfo signingInfo) {
		if (signingInfo == null) {
			throw Problem.builder()
				.withTitle("Bad Request")
				.withStatus(BAD_REQUEST)
				.withDetail("signingInfo is required for non-whitelisted mandates")
				.build();
		}

		final var violations = validator.validate(signingInfo);
		if (!violations.isEmpty()) {
			final var message = violations.stream()
				.map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
				.collect(Collectors.joining(", "));
			throw Problem.builder()
				.withTitle("Bad Request")
				.withStatus(BAD_REQUEST)
				.withDetail("Invalid signingInfo: " + message)
				.build();
		}
	}
}
