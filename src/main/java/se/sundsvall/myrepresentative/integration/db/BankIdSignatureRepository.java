package se.sundsvall.myrepresentative.integration.db;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.jpa.repository.JpaRepository;
import se.sundsvall.myrepresentative.integration.db.entity.BankIdSignatureEntity;

@CircuitBreaker(name = "bankIdSignatureRepository")
public interface BankIdSignatureRepository extends JpaRepository<BankIdSignatureEntity, Long> {
}
