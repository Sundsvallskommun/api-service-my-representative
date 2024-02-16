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

	public List<MandateTemplate> getTemplates() {
		return repository.findAll().stream().map(MandateTemplateMapper::toTemplate).toList();
	}

	public MandateTemplate getTemplate(final String id) {

		return MandateTemplateMapper.toTemplate(repository.findById(id).orElseThrow(() -> Problem.valueOf(Status.NOT_FOUND, "Could not find template with id %s".formatted(id))));
	}

	public String createTemplate(final MandateTemplate template) {

		final var result = repository.save(MandateTemplateMapper.toEntity(template));

		return result.getCode();
	}

	public void updateTemplate(final String id, final MandateTemplate template) {

		final var entity = repository.findById(id).orElseThrow(() -> Problem.valueOf(Status.NOT_FOUND, "Could not find template with id %s to update".formatted(id)));
		MandateTemplateMapper.updateEntity(entity, template);
		repository.save(entity);
	}

	public void deleteTemplate(final String id) {

		repository.deleteById(id);
	}

	public String getDescriptionForTemplate(final String id) {

		return repository.findById(id)
			.map(MandateTemplateEntity::getDescription)
			.orElse(null);
	}

}
