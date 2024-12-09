package se.sundsvall.myrepresentative.service.mandatetemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.Problem;
import se.sundsvall.myrepresentative.api.model.MandateTemplate;
import se.sundsvall.myrepresentative.integration.db.MandateTemplateRepository;
import se.sundsvall.myrepresentative.integration.db.model.MandateTemplateEntity;

@ExtendWith(MockitoExtension.class)
class MandateTemplateServiceTest {

	@InjectMocks
	private MandateTemplateService service;

	@Mock
	private MandateTemplateRepository repositoryMock;

	@Test
	void getTemplates() {
		// Mock
		final var expectedTemplates = List.of(MandateTemplateEntity.builder()
			.withTitle("someTitle")
			.withCode("someCode")
			.withDescription("someDescription")
			.build());
		when(repositoryMock.findAllByMunicipalityId(MUNICIPALITY_ID)).thenReturn(expectedTemplates);
		// Act
		final var result = service.getTemplates(MUNICIPALITY_ID);
		// Assert
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.getFirst()).hasNoNullFieldsOrProperties();
		assertThat(result.getFirst().getCode()).isEqualTo("someCode");
		assertThat(result.getFirst().getDescription()).isEqualTo("someDescription");
		assertThat(result.getFirst().getTitle()).isEqualTo("someTitle");

		verify(repositoryMock).findAllByMunicipalityId(MUNICIPALITY_ID);

	}

	@Test
	void getTemplate() {
		// Mock
		final var expectedTemplate = MandateTemplateEntity.builder()
			.withTitle("someTitle")
			.withCode("someCode")
			.withDescription("someDescription")
			.build();
		when(repositoryMock.findByMunicipalityIdAndCode(eq(MUNICIPALITY_ID), anyString())).thenReturn(Optional.ofNullable(expectedTemplate));
		// Act
		final var result = service.getTemplate(MUNICIPALITY_ID, "someCode");
		// Assert
		assertThat(result).isNotNull().hasNoNullFieldsOrProperties();
		assertThat(result.getCode()).isEqualTo("someCode");
		assertThat(result.getDescription()).isEqualTo("someDescription");
		assertThat(result.getTitle()).isEqualTo("someTitle");

		verify(repositoryMock).findByMunicipalityIdAndCode(eq(MUNICIPALITY_ID), anyString());

	}

	@Test
	void getTemplate_NotFound() {
		assertThatThrownBy(() -> service.getTemplate(MUNICIPALITY_ID, "someCode"))
			.isInstanceOf(Problem.class)
			.hasMessage("Not Found: Could not find template with municipalityId 2281 and id someCode");
	}

	@Test
	void createTemplate() {
		// Mock
		when(repositoryMock.save(any(MandateTemplateEntity.class))).thenReturn(MandateTemplateEntity.builder().withCode("someCode").build());
		// Act
		final var expectedTemplate = MandateTemplate.builder()
			.withTitle("someTitle")
			.withCode("someCode")
			.withDescription("someDescription")
			.build();

		final var result = service.createTemplate(MUNICIPALITY_ID, expectedTemplate);
		// Assert
		assertThat(result).isEqualTo("someCode");
	}

	@Test
	void updateTemplate() {
		// Mock
		when(repositoryMock.findByMunicipalityIdAndCode(eq(MUNICIPALITY_ID), anyString())).thenReturn(Optional.of(MandateTemplateEntity.builder()
			.withTitle("someTitle")
			.withCode("someCode")
			.withDescription("someDescription")
			.build()));
		// Act
		final var updatedTemplate = MandateTemplate.builder()
			.withTitle("someUpdatedTitle")
			.withCode("someUpdatedCode")
			.withDescription("someUpdatedDescription")
			.build();

		service.updateTemplate(MUNICIPALITY_ID, "someCode", updatedTemplate);
		// Assert
		verify(repositoryMock).findByMunicipalityIdAndCode(eq(MUNICIPALITY_ID), anyString());
		verify(repositoryMock).save(any(MandateTemplateEntity.class));
		verifyNoMoreInteractions(repositoryMock);
	}

	@Test
	void updateTemplate_notfound() {

		assertThatThrownBy(() -> service.updateTemplate(MUNICIPALITY_ID, "someString", MandateTemplate.builder().build()))
			.isInstanceOf(Problem.class)
			.hasMessage("Not Found: Could not find template with municipalityId 2281 and id someString to update");
	}

	@Test
	void deleteTemplate() {
		// Act
		final var id = "someCode";
		service.deleteTemplate(MUNICIPALITY_ID, id);
		// Assert
		verify(repositoryMock).deleteByMunicipalityIdAndCode(MUNICIPALITY_ID, id);
		verifyNoMoreInteractions(repositoryMock);
	}

	@Test
	void getDescriptionForTemplate() {
		// Mock
		when(repositoryMock.findByMunicipalityIdAndCode(eq(MUNICIPALITY_ID), anyString())).thenReturn(Optional.of(MandateTemplateEntity.builder()
			.withTitle("someTitle")
			.withCode("someCode")
			.withDescription("someDescription")
			.build()));

		// Act
		final var result = service.getDescriptionForTemplate(MUNICIPALITY_ID, "someCode");

		// Assert
		assertThat(result).isEqualTo("someDescription");

	}

	@Test
	void getDescriptionForTemplate_notfound() {
		// Act
		final var result = service.getDescriptionForTemplate(MUNICIPALITY_ID, "someCode");
		// Assert
		assertThat(result).isNull();

	}

}
