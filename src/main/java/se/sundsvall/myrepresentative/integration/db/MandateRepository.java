package se.sundsvall.myrepresentative.integration.db;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;

@CircuitBreaker(name = "mandateRepository")
public interface MandateRepository extends JpaRepository<MandateEntity, String> {

	Optional<MandateEntity> findByIdAndMunicipalityIdAndNamespace(String id, String municipalityId, String namespace);

	Optional<MandateEntity> findByIdAndMunicipalityIdAndNamespaceAndDeletedIs(String id, String municipalityId, String namespace, String deleted);

	// Convenience method for finding non-deleted mandates
	default Optional<MandateEntity> findActiveByIdAndMunicipalityIdAndNamespace(String id, String municipalityId, String namespace) {
		return findByIdAndMunicipalityIdAndNamespaceAndDeletedIs(id, municipalityId, namespace, MandateEntity.NOT_DELETED);
	}
}
