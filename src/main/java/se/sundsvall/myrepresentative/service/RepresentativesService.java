package se.sundsvall.myrepresentative.service;

import static org.zalando.problem.Status.NOT_FOUND;

import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.MandateDetails;
import se.sundsvall.myrepresentative.config.DataIntegrityExceptionHandler;
import se.sundsvall.myrepresentative.integration.db.MandateRepository;

@Service
public class RepresentativesService {

	private final MandateRepository mandateRepository;
	private final Mapper mapper;

	public RepresentativesService(MandateRepository mandateRepository, Mapper mapper) {
		this.mandateRepository = mandateRepository;
		this.mapper = mapper;
	}

	/**
	 * Creates a new mandate.
	 * Throws a Problem with status 409 if a mandate already exists with the same municipalityId, namespace, grantorPartyId,
	 * granteePartyId,
	 * deleted value and overlapping activeFrom/inactiveAfter period. Handled in the {@link DataIntegrityExceptionHandler}.
	 * 
	 * @param  municipalityId municipalityId
	 * @param  namespace      namespace in which the mandate should be created
	 * @param  request        the mandate to create
	 * @return                the id of the created mandate
	 */
	public String createMandate(final String municipalityId, final String namespace, final CreateMandate request) {
		final var mandateEntity = mapper.toMandateEntity(municipalityId, namespace, request);

		final var saved = mandateRepository.save(mandateEntity);

		return saved.getId();
	}

	/**
	 * Retrieves the mandate for the given id, municipalityId and namespace.
	 * Throws a Problem with status 404 if no mandate is found with the given id
	 * 
	 * @param  municipalityId municipalityId
	 * @param  namespace      namespace
	 * @param  mandateId      the id of the mandate to retrieve
	 * @return                the mandate details
	 */
	public MandateDetails getMandateDetails(final String municipalityId, final String namespace, final String mandateId) {
		return mandateRepository.findByIdAndMunicipalityIdAndNamespace(mandateId, municipalityId, namespace)
			.map(mapper::toMandateDetails)
			.orElseThrow(() -> Problem.builder()
				.withTitle("No mandate found")
				.withStatus(NOT_FOUND)
				.withDetail("Couldn't find any mandate with id '" + mandateId + "' for municipality '" + municipalityId +
					"' and namespace '" + namespace + "'")
				.build());
	}
}
