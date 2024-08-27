package se.sundsvall.myrepresentative.integration.db;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import se.sundsvall.myrepresentative.integration.db.model.MandateTemplateEntity;

public interface MandateTemplateRepository extends JpaRepository<MandateTemplateEntity, String> {

	List<MandateTemplateEntity> findAllByMunicipalityId(final String municipalityId);

	Optional<MandateTemplateEntity> findByMunicipalityIdAndCode(final String municipalityId, final String code);

	void deleteByMunicipalityIdAndCode(final String municipalityId, final String code);

}
