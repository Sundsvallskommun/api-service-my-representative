package se.sundsvall.myrepresentative.service.mandatetemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;

import org.junit.jupiter.api.Test;
import se.sundsvall.myrepresentative.api.model.MandateTemplate;
import se.sundsvall.myrepresentative.integration.db.model.MandateTemplateEntity;

class MandateTemplateMapperTest {

	@Test
	void toTemplate() {
		// Arrange
		final var code = "someCode";
		final var title = "someTitle";
		final var description = "someDescription";

		final var entity = MandateTemplateEntity.builder()
			.withTitle(title)
			.withCode(code)
			.withDescription("someDescription")
			.build();

		// Act
		final var result = MandateTemplateMapper.toTemplate(entity);

		// Assert
		assertThat(result.getTitle()).isEqualTo(title);
		assertThat(result.getCode()).isEqualTo(code);
		assertThat(result.getDescription()).isEqualTo(description);

	}

	@Test
	void toEntity() {
		// Arrange
		final var code = "someCode";
		final var title = "someTitle";
		final var description = "someDescription";

		final var template = MandateTemplate.builder()
			.withTitle(title)
			.withCode(code)
			.withDescription("someDescription")
			.build();

		// Act
		final var result = MandateTemplateMapper.toEntity(template, MUNICIPALITY_ID);

		// Assert
		assertThat(result.getTitle()).isEqualTo(title);
		assertThat(result.getCode()).isEqualTo(code);
		assertThat(result.getDescription()).isEqualTo(description);
	}

	@Test
	void updateEntity() {

		// Arrange
		final var code = "someCode";
		final var title = "someTitle";
		final var description = "someDescription";
		final var otherCode = "someOtherCode";
		final var otherTitle = "someOtherTitle";
		final var otherDescription = "someOtherDescription";

		final var entityToUpdate = MandateTemplateEntity.builder()
			.withTitle(title)
			.withCode(code)
			.withDescription(description)
			.build();

		final var templateToUpdateTo = MandateTemplate.builder()
			.withTitle(otherTitle)
			.withCode(otherCode)
			.withDescription(otherDescription)
			.build();

		// Act
		MandateTemplateMapper.updateEntity(entityToUpdate, templateToUpdateTo);

		// Assert
		assertThat(entityToUpdate.getTitle()).isEqualTo(otherTitle);
		assertThat(entityToUpdate.getDescription()).isEqualTo(otherDescription);
		assertThat(entityToUpdate.getCode()).isEqualTo(otherCode);
	}

}
