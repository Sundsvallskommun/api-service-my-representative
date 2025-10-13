package se.sundsvall.myrepresentative.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;
import static se.sundsvall.myrepresentative.TestObjectFactory.createMandate;

import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.myrepresentative.api.model.CreateMandateBuilder;
import se.sundsvall.myrepresentative.api.model.MandateDetails;
import se.sundsvall.myrepresentative.api.model.MandateDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.Mandates;
import se.sundsvall.myrepresentative.api.model.MandatesBuilder;
import se.sundsvall.myrepresentative.api.model.SearchMandateParameters;
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
	void testCreateMandateWithNoEndDateShouldBeValid() {
		final var id = UUID.randomUUID().toString();
		final var createMandate = CreateMandateBuilder.from(createMandate()).withInactiveAfter(null).build();
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
		final var mandateDetails = MandateDetailsBuilder.create().build();

		when(mockService.getMandateDetails(MUNICIPALITY_ID, NAMESPACE, id)).thenReturn(MandateDetailsBuilder.create().build());

		var response = webTestClient.get()
			.uri(uriBuilder -> uriBuilder.path(url)
				.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "id", id)))
			.exchange()
			.expectStatus().isOk()
			.expectBody(MandateDetails.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isEqualTo(mandateDetails);
		verify(mockService).getMandateDetails(MUNICIPALITY_ID, NAMESPACE, id);
	}

	@Test
	void testDeleteMandates() {
		final var url = BASE_URL + "/{id}";
		final var id = UUID.randomUUID().toString();

		doNothing().when(mockService).deleteMandate(MUNICIPALITY_ID, NAMESPACE, id);

		webTestClient.delete()
			.uri(uriBuilder -> uriBuilder.path(url)
				.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "id", id)))
			.exchange()
			.expectStatus().isNoContent();

		verify(mockService).deleteMandate(MUNICIPALITY_ID, NAMESPACE, id);
	}

	@Test
	void testSearchMandates() {
		var parameters = new SearchMandateParameters()
			.withGranteePartyId(UUID.randomUUID().toString())
			.withGrantorPartyId(UUID.randomUUID().toString())
			.withSignatoryPartyId(UUID.randomUUID().toString())
			.withPage(1)
			.withLimit(15);

		final var mandates = MandatesBuilder.create().build();

		when(mockService.searchMandates(MUNICIPALITY_ID, NAMESPACE, parameters)).thenReturn(MandatesBuilder.create().build());

		var response = webTestClient.get()
			.uri(uriBuilder -> uriBuilder.path(BASE_URL)
				.queryParam("granteePartyId", parameters.getGranteePartyId())
				.queryParam("grantorPartyId", parameters.getGrantorPartyId())
				.queryParam("signatoryPartyId", parameters.getSignatoryPartyId())
				.queryParam("page", parameters.getPage())
				.queryParam("limit", parameters.getLimit())
				.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE)))
			.exchange()
			.expectStatus().isOk()
			.expectBody(Mandates.class)
			.returnResult()
			.getResponseBody();

		assertThat(response).isEqualTo(mandates);
		verify(mockService).searchMandates(MUNICIPALITY_ID, NAMESPACE, parameters);
	}
}
