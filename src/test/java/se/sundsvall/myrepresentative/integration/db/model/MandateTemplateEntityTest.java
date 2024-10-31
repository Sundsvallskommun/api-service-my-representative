package se.sundsvall.myrepresentative.integration.db.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class MandateTemplateEntityTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(MandateTemplateEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilder() {

		// Arrange
		final var code = "someCode";
		final var title = "someTitle";
		final var description = "someDescription";
		final var municipalityId = "2281";

		// Act
		final var result = MandateTemplateEntity.builder()
			.withCode(code)
			.withTitle(title)
			.withDescription(description)
			.withMunicipalityId(municipalityId)
			.build();

		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getCode()).isEqualTo(code);
		assertThat(result.getTitle()).isEqualTo(title);
		assertThat(result.getDescription()).isEqualTo(description);
		assertThat(result.getMunicipalityId()).isEqualTo(municipalityId);
	}

}
