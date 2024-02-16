package se.sundsvall.myrepresentative.integration.db;

import org.springframework.data.jpa.repository.JpaRepository;

import se.sundsvall.myrepresentative.integration.db.model.MandateTemplateEntity;

public interface MandateTemplateRepository extends JpaRepository<MandateTemplateEntity, String> {

}
