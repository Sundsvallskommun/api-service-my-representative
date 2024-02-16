package se.sundsvall.myrepresentative.service.mandatetemplate;

import se.sundsvall.myrepresentative.api.model.MandateTemplate;
import se.sundsvall.myrepresentative.integration.db.model.MandateTemplateEntity;

public final class MandateTemplateMapper {

	private MandateTemplateMapper() {
		//Intentionally left empty
	}

	public static MandateTemplate toTemplate(final MandateTemplateEntity entity) {
		return MandateTemplate.builder()
			.withCode(entity.getCode())
			.withTitle(entity.getTitle())
			.withDescription(entity.getDescription())
			.build();
	}

	public static MandateTemplateEntity toEntity(final MandateTemplate template) {

		return MandateTemplateEntity.builder()
			.withCode(template.getCode())
			.withTitle(template.getTitle())
			.withDescription(template.getDescription())
			.build();
	}

	public static void updateEntity(final MandateTemplateEntity entity, final MandateTemplate template) {

		entity.setCode(template.getCode());
		entity.setDescription(template.getDescription());
		entity.setTitle(template.getTitle());
	}

}
