package se.sundsvall.myrepresentative.integration.db;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import se.sundsvall.myrepresentative.integration.db.model.MandateTemplateEntity;

@CircuitBreaker(name = "MandateTemplateRepository")
public interface MandateTemplateRepository extends JpaRepository<MandateTemplateEntity, String> {

	List<MandateTemplateEntity> findAllByMunicipalityId(final String municipalityId);

	Optional<MandateTemplateEntity> findByMunicipalityIdAndCode(final String municipalityId, final String code);

	void deleteByMunicipalityIdAndCode(final String municipalityId, final String code);

}
