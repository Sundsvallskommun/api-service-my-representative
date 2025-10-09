package se.sundsvall.myrepresentative.integration.db;

import static se.sundsvall.myrepresentative.integration.db.specification.MandateSpecification.withGranteePartyId;
import static se.sundsvall.myrepresentative.integration.db.specification.MandateSpecification.withGrantorPartyId;
import static se.sundsvall.myrepresentative.integration.db.specification.MandateSpecification.withMunicipalityId;
import static se.sundsvall.myrepresentative.integration.db.specification.MandateSpecification.withNamespace;
import static se.sundsvall.myrepresentative.integration.db.specification.MandateSpecification.withSignatoryPartyId;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import se.sundsvall.myrepresentative.api.model.SearchMandateParameters;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;

@CircuitBreaker(name = "mandateRepository")
public interface MandateRepository extends JpaRepository<MandateEntity, String>, JpaSpecificationExecutor<MandateEntity> {

	Optional<MandateEntity> findByMunicipalityIdAndNamespaceAndId(final String municipalityId, final String namespace, final String id);

	Optional<MandateEntity> findByMunicipalityIdAndNamespaceAndIdAndDeletedIs(final String municipalityId, final String namespace, String id, final OffsetDateTime deleted);

	// Convenience method for finding non-deleted mandates
	default Optional<MandateEntity> findActiveByMunicipalityIdAndNamespaceAndId(final String municipalityId, final String namespace, final String id) {
		return findByMunicipalityIdAndNamespaceAndIdAndDeletedIs(municipalityId, namespace, id, OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC));
	}

	default Page<MandateEntity> findAllWithParameters(final String municipalityId, final String namespace, final SearchMandateParameters parameters, final Pageable pageable) {
		final var sort = Sort.by(
			Sort.Order.asc("status"),
			Sort.Order.desc("created"));
		final var effectivePageable = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), sort);

		return this.findAll(
			Specification.allOf(
				withMunicipalityId(municipalityId)
					.and(withNamespace(namespace))
					.and(withGrantorPartyId(parameters.getGrantorPartyId()))
					.and(withGranteePartyId(parameters.getGranteePartyId()))
					.and(withSignatoryPartyId(parameters.getSignatoryPartyId()))),
			effectivePageable);
	}
}
