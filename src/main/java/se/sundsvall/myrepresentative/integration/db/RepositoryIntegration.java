package se.sundsvall.myrepresentative.integration.db;

import static java.time.temporal.ChronoUnit.MILLIS;
import static se.sundsvall.dept44.util.LogUtils.sanitizeForLogging;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.SearchMandateParameters;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;

@Component
public class RepositoryIntegration {

	private static final Logger LOG = LoggerFactory.getLogger(RepositoryIntegration.class);

	private final MandateRepository mandateRepository;
	private final DatabaseMapper mapper;

	public RepositoryIntegration(MandateRepository mandateRepository, DatabaseMapper mapper) {
		this.mandateRepository = mandateRepository;
		this.mapper = mapper;
	}

	public MandateEntity createMandate(String municipalityId, String namespace, CreateMandate request) {
		var mandateEntity = mapper.toMandateEntity(municipalityId, namespace, request);
		return mandateRepository.save(mandateEntity);
	}

	public Optional<MandateEntity> getMandateDetails(final String municipalityId, final String namespace, final String mandateId) {
		return mandateRepository.findByMunicipalityIdAndNamespaceAndId(municipalityId, namespace, mandateId);
	}

	public void deleteMandate(final String municipalityId, final String namespace, final String id) {
		// We don't delete the entity, we just mark it as deleted using a timestamp to be able to keep them unique.
		mandateRepository.findActiveByMunicipalityIdAndNamespaceAndId(municipalityId, namespace, sanitizeForLogging(id))
			.ifPresentOrElse(mandateEntity -> {
				LOG.info("Soft deleting mandate with id {}", sanitizeForLogging(id));
				mandateEntity.withDeleted(OffsetDateTime.now(ZoneId.systemDefault()).truncatedTo(MILLIS));
				mandateRepository.save(mandateEntity);
			}, () -> LOG.info("No mandate found with id {}, nothing to delete", sanitizeForLogging(id)));
	}

	public Page<MandateEntity> searchMandates(final String municipalityId, final String namespace, SearchMandateParameters parameters) {
		var pageable = PageRequest.of(parameters.getPage(), parameters.getLimit());
		return mandateRepository.findAllWithParameters(municipalityId, namespace, parameters, pageable);
	}
}
