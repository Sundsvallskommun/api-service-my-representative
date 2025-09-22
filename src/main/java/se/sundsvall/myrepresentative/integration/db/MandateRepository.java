package se.sundsvall.myrepresentative.integration.db;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.jpa.repository.JpaRepository;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;

@CircuitBreaker(name = "mandateRepository")
public interface MandateRepository extends JpaRepository<MandateEntity, String> {
}
