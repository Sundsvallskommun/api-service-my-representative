package se.sundsvall.myrepresentative.service;

import static org.zalando.problem.Status.NOT_FOUND;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.MandateDetails;
import se.sundsvall.myrepresentative.api.model.Mandates;
import se.sundsvall.myrepresentative.api.model.SearchMandateParameters;
import se.sundsvall.myrepresentative.config.DataIntegrityExceptionHandler;
import se.sundsvall.myrepresentative.integration.db.RepositoryIntegration;
import se.sundsvall.myrepresentative.integration.legalentity.configuration.LegalEntityProperties;

@Service
public class RepresentativesService {

	private static final Logger LOG = LoggerFactory.getLogger(RepresentativesService.class);

	private final RepositoryIntegration repositoryIntegration;
	private final LegalEntityService legalEntityService;
	private final ServiceMapper serviceMapper;
	private final LegalEntityProperties legalEntityProperties;

	public RepresentativesService(final RepositoryIntegration repositoryIntegration, final LegalEntityService legalEntityService, final ServiceMapper serviceMapper, final LegalEntityProperties legalEntityProperties) {
		this.legalEntityService = legalEntityService;
		this.repositoryIntegration = repositoryIntegration;
		this.serviceMapper = serviceMapper;
		this.legalEntityProperties = legalEntityProperties;
	}

	/**
	 * Creates a new mandate after validating that the signatory is an authorized signatory for the organization.
	 * Throws a Problem with status 409 if a mandate already exists with the same municipalityId, namespace, grantorPartyId,
	 * granteePartyId,
	 * deleted value and overlapping activeFrom/inactiveAfter period. Handled in the {@link DataIntegrityExceptionHandler}.
	 * Validation can be disabled through configuration.
	 *
	 * @param  municipalityId municipalityId
	 * @param  namespace      namespace in which the mandate should be created
	 * @param  request        the mandate to create
	 * @return                the id of the created mandate
	 */
	public String createMandate(final String municipalityId, final String namespace, final CreateMandate request) {
		if (legalEntityProperties.validationEnabled()) {
			legalEntityService.validateSignatory(municipalityId, request);
		} else {
			// Log a warning that legal entity validation is disabled
			LOG.warn("Signatory validation is disabled");
		}

		final var mandateEntity = repositoryIntegration.createMandate(municipalityId, namespace, request);

		return mandateEntity.getId();
	}

	/**
	 * Retrieves the mandate for the given id, municipalityId and namespace.
	 * Throws a Problem with status 404 if no mandate is found with the given id
	 *
	 * @param  municipalityId municipalityId
	 * @param  namespace      namespace
	 * @param  id             the id of the mandate to retrieve
	 * @return                the mandate details
	 */
	public MandateDetails getMandateDetails(final String municipalityId, final String namespace, final String id) {
		final var mandateEntity = repositoryIntegration.getMandateDetails(municipalityId, namespace, id);

		return mandateEntity
			.map(serviceMapper::toMandateDetailsWithSigningInfo)
			.orElseThrow(() -> Problem.builder()
				.withTitle("No mandate found")
				.withStatus(NOT_FOUND)
				.withDetail("Couldn't find any mandate with id '" + id + "' for municipality '" + municipalityId +
					"' and namespace '" + namespace + "'")
				.build());
	}

	/**
	 * Soft deletes the mandate for the given id, municipalityId and namespace.
	 * The soft delete is done by setting the 'deleted' attribute to the current timestamp.
	 *
	 * @param municipalityId municipalityId
	 * @param namespace      namespace
	 * @param id             id of the mandate to soft delete
	 */
	public void deleteMandate(final String municipalityId, final String namespace, final String id) {
		repositoryIntegration.deleteMandate(municipalityId, namespace, id);
	}

	public Mandates searchMandates(final String municipalityId, final String namespace, final SearchMandateParameters parameters) {
		final var foundMandates = repositoryIntegration.searchMandates(municipalityId, namespace, parameters);
		return serviceMapper.toMandates(foundMandates);
	}
}
