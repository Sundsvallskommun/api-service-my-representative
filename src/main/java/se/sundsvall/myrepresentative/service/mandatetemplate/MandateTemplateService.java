package se.sundsvall.myrepresentative.service.mandatetemplate;


import java.util.List;

import org.springframework.stereotype.Service;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import se.sundsvall.myrepresentative.api.model.MandateTemplate;
import se.sundsvall.myrepresentative.integration.db.MandateTemplateRepository;
import se.sundsvall.myrepresentative.integration.db.model.MandateTemplateEntity;

@Service
public class MandateTemplateService {

	private final MandateTemplateRepository repository;

	public MandateTemplateService(final MandateTemplateRepository repository) {this.repository = repository;}

	public List<MandateTemplate> getTemplates(final String municipalityId) {
		return repository.findAllByMunicipalityId(municipalityId).stream().map(MandateTemplateMapper::toTemplate).toList();
	}

	public MandateTemplate getTemplate(final String municipalityId, final String id) {

		return MandateTemplateMapper.toTemplate(repository.findByMunicipalityIdAndCode(municipalityId, id).orElseThrow(() -> Problem.valueOf(Status.NOT_FOUND, "Could not find template with municipalityId %s and id %s".formatted(municipalityId, id))));
	}

	public String createTemplate(final String municipalityId, final MandateTemplate template) {

		final var result = repository.save(MandateTemplateMapper.toEntity(template, municipalityId));

		return result.getCode();
	}

	public void updateTemplate(final String municipalityId, final String id, final MandateTemplate template) {

		final var entity = repository.findByMunicipalityIdAndCode(municipalityId, id).orElseThrow(() -> Problem.valueOf(Status.NOT_FOUND, "Could not find template with municipalityId %s and id %s to update".formatted(municipalityId, id)));
		MandateTemplateMapper.updateEntity(entity, template);
		repository.save(entity);
	}

	public void deleteTemplate(final String municipalityId, final String id) {

		repository.deleteByMunicipalityIdAndCode(municipalityId, id);
	}

	public String getDescriptionForTemplate(final String municipalityId, final String id) {

		return repository.findByMunicipalityIdAndCode(municipalityId, id)
			.map(MandateTemplateEntity::getDescription)
			.orElse(null);
	}

}
