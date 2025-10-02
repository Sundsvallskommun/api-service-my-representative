package se.sundsvall.myrepresentative.api;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;
import static se.sundsvall.myrepresentative.TestObjectFactory.createMandate;
import static se.sundsvall.myrepresentative.TestObjectFactory.updateMandate;

import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.myrepresentative.api.model.MandateDetails;
import se.sundsvall.myrepresentative.api.model.MandateDetailsBuilder;
import se.sundsvall.myrepresentative.service.RepresentativesService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class MandatesResourceTest {

	private static final String BASE_URL = "/{municipalityId}/{namespace}/mandates";

	@MockitoBean
	private RepresentativesService mockService;

	@Autowired
	private WebTestClient webTestClient;

	@AfterEach
	void tearDown() {
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void testCreateMandate() {
		final var id = UUID.randomUUID().toString();
		final var createMandate = createMandate();
		when(mockService.createMandate(MUNICIPALITY_ID, NAMESPACE, createMandate)).thenReturn(id);

		webTestClient.post()
			.uri(uriBuilder -> uriBuilder.path(BASE_URL)
				.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE)))
			.bodyValue(createMandate)
			.exchange()
			.expectStatus().isCreated()
			.expectHeader().valueEquals("Location", String.format("/%s/%s/mandates/%s", MUNICIPALITY_ID, NAMESPACE, id));

		verify(mockService).createMandate(MUNICIPALITY_ID, NAMESPACE, createMandate);
	}

	@Test
	void testGetMandateById() {
		final var url = BASE_URL + "/{id}";
		final var id = UUID.randomUUID().toString();

		when(mockService.getMandateDetails(MUNICIPALITY_ID, NAMESPACE, id)).thenReturn(MandateDetailsBuilder.create().build());

		webTestClient.get()
			.uri(uriBuilder -> uriBuilder.path(url)
				.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "id", id)))
			.exchange()
			.expectStatus().isOk()
			.expectBody(MandateDetails.class);

		verify(mockService).getMandateDetails(MUNICIPALITY_ID, NAMESPACE, id);
	}

	// Unimplemented methods
	@Test
	void testSearchMandates() {
		webTestClient.get()
			.uri(uriBuilder -> uriBuilder.path(BASE_URL)
				.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE)))
			.exchange()
			.expectStatus().isEqualTo(HttpStatus.NOT_IMPLEMENTED);

		verifyNoInteractions(mockService);
	}

	@Test
	void testUpdateMandates() {
		final var url = BASE_URL + "/{id}";
		final var id = UUID.randomUUID().toString();
		final var updateMandate = updateMandate();

		webTestClient.patch()
			.uri(uriBuilder -> uriBuilder.path(url)
				.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "id", id)))
			.bodyValue(updateMandate)
			.exchange()
			.expectStatus().isEqualTo(HttpStatus.NOT_IMPLEMENTED);

		verifyNoInteractions(mockService);
	}

	@Test
	void testDeleteMandates() {
		final var url = BASE_URL + "/{id}";
		final var id = UUID.randomUUID().toString();

		webTestClient.delete()
			.uri(uriBuilder -> uriBuilder.path(url)
				.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "id", id)))
			.exchange()
			.expectStatus().isEqualTo(HttpStatus.NOT_IMPLEMENTED);

		verifyNoInteractions(mockService);
	}
}
