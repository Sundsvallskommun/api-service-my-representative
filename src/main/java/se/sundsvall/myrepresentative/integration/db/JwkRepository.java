package se.sundsvall.myrepresentative.integration.db;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.jpa.repository.JpaRepository;
import se.sundsvall.myrepresentative.integration.db.model.JwkEntity;

import java.time.OffsetDateTime;

@CircuitBreaker(name = "JwkRepository")
public interface JwkRepository extends JpaRepository<JwkEntity, Long> {
	void deleteByValidUntilBefore(OffsetDateTime offsetDateTime);
	boolean existsByValidUntilAfter(OffsetDateTime offsetDateTime);
}
