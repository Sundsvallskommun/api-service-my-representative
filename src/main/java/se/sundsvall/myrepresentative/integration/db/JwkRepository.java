package se.sundsvall.myrepresentative.integration.db;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import se.sundsvall.myrepresentative.integration.db.model.JwkEntity;

@CircuitBreaker(name = "JwkRepository")
public interface JwkRepository extends JpaRepository<JwkEntity, Long> {
	void deleteByValidUntilBefore(final OffsetDateTime offsetDateTime);

	boolean existsByValidUntilAfter(final OffsetDateTime offsetDateTime);

	List<JwkEntity> findByMunicipalityIdAndValidUntilAfter(final String municipalityId, final OffsetDateTime offsetDateTime);
}
