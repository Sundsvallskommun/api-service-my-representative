package se.sundsvall.myrepresentative.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import se.sundsvall.myrepresentative.MyRepresentatives;
import se.sundsvall.myrepresentative.api.model.MandateTemplate;
import se.sundsvall.myrepresentative.service.mandatetemplate.MandateTemplateService;

@SpringBootTest(classes = MyRepresentatives.class, webEnvironment = RANDOM_PORT)
@ActiveProfiles("junit")
@DirtiesContext // JWTServiceTest#testCreateJwtAndVerifyHeaderAndPayload requires this
class MandateTemplateResourceTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private MandateTemplateService mandateTemplateServiceMock;

	@Test
	void getMandateTemplates() {
		// Mock
		when(mandateTemplateServiceMock.getTemplates(MUNICIPALITY_ID)).thenReturn(List.of(MandateTemplate.builder()
			.withDescription("someDescription")
			.withTitle("someTitle")
			.withCode(UUID.randomUUID().toString())
			.build()));

		//Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path("/{municipalityId}/mandates/templates").build(Map.of("municipalityId", MUNICIPALITY_ID)))
			.exchange()
			.expectStatus().isOk()
			.expectBodyList(MandateTemplate.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull().isNotEmpty().hasSize(1);
		assertThat(response.getFirst()).hasNoNullFieldsOrProperties();
		verify(mandateTemplateServiceMock).getTemplates(MUNICIPALITY_ID);
		verifyNoMoreInteractions(mandateTemplateServiceMock);
	}

	@Test
	void getMandateTemplate() {
		// Mock
		when(mandateTemplateServiceMock.getTemplate(eq(MUNICIPALITY_ID), anyString())).thenReturn(MandateTemplate.builder()
			.withDescription("someDescription")
			.withTitle("someTitle")
			.withCode(UUID.randomUUID().toString())
			.build());

		// Act
		final var response = webTestClient.get()
			.uri(builder -> builder.path("/{municipalityId}/mandates/templates/{id}").build(Map.of("municipalityId", MUNICIPALITY_ID, "id", "someId")))
			.exchange()
			.expectStatus().isOk()
			.expectBody(MandateTemplate.class)
			.returnResult()
			.getResponseBody();

		// Assert
		assertThat(response).isNotNull().hasNoNullFieldsOrProperties();
		verify(mandateTemplateServiceMock).getTemplate(eq(MUNICIPALITY_ID), anyString());
		verifyNoMoreInteractions(mandateTemplateServiceMock);
	}

	@Test
	void createMandateTemplate() {
		// Mock
		when(mandateTemplateServiceMock.createTemplate(eq(MUNICIPALITY_ID), any(MandateTemplate.class))).thenReturn("someValue");

		//Act
		final var response = webTestClient
			.post()
			.uri(builder -> builder.path("/{municipalityId}/mandates/templates").build(Map.of("municipalityId", MUNICIPALITY_ID)))
			.bodyValue(MandateTemplate.builder().build())
			.exchange()
			.expectStatus().isCreated()
			.expectBody(Void.class)
			.returnResult()
			.getResponseHeaders();

		// Assert
		assertThat(response).isNotNull().containsEntry("location", List.of("/" + MUNICIPALITY_ID + "/mandates/templates/someValue"));
		verify(mandateTemplateServiceMock).createTemplate(eq(MUNICIPALITY_ID), any(MandateTemplate.class));
		verifyNoMoreInteractions(mandateTemplateServiceMock);
	}

	@Test
	void updateMandateTemplate() {
		//Act
		webTestClient
			.put()
			.uri(builder -> builder.path("/{municipalityId}/mandates/templates/{id}").build(Map.of("municipalityId", MUNICIPALITY_ID, "id", "someId")))
			.bodyValue(MandateTemplate.builder().build())
			.exchange()
			.expectStatus().isNoContent()
			.expectBody().isEmpty();

		// Assert
		verify(mandateTemplateServiceMock).updateTemplate(eq(MUNICIPALITY_ID), anyString(), any(MandateTemplate.class));
	}

	@Test
	void deleteMandateTemplate() {
		// Act
		webTestClient.delete()
			.uri(builder -> builder.path("/{municipalityId}/mandates/templates/{id}").build(Map.of("municipalityId", MUNICIPALITY_ID, "id", "someId")))
			.exchange()
			.expectStatus().isNoContent()
			.expectBody().isEmpty();

		// Assert
		verify(mandateTemplateServiceMock).deleteTemplate(eq(MUNICIPALITY_ID), anyString());

	}

}
