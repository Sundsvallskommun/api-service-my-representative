package se.sundsvall.myrepresentative.api;

import java.util.Map;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import se.sundsvall.dept44.problem.Problem;
import se.sundsvall.dept44.problem.violations.ConstraintViolationProblem;
import se.sundsvall.dept44.problem.violations.Violation;
import se.sundsvall.myrepresentative.service.RepresentativesService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("junit")
class GetMandatesResourceFailureTest {

	private static final String BASE_URL = "/{municipalityId}/{namespace}/mandates/{id}";

	@MockitoBean
	private RepresentativesService mockService;

	@Autowired
	private WebTestClient webTestClient;

	@AfterEach
	void tearDown() {
		verifyNoMoreInteractions(mockService);
	}

	@Test
	void testFaultyIdShouldReturn400() {
		final var invalidId = "not-a-uuid";

		final var problem = webTestClient.get()
			.uri(uriBuilder -> uriBuilder.path(BASE_URL)
				.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "id", invalidId)))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(problem).isNotNull().satisfies(p -> {
			assertThat(p.getTitle()).isEqualTo("Constraint Violation");
			assertThat(p.getStatus()).isEqualTo(BAD_REQUEST);
			assertThat(p.getViolations())
				.extracting(Violation::field, Violation::message)
				.containsExactlyInAnyOrder(
					Assertions.tuple("getMandateById.id", "not a valid UUID"));
		});
	}

	@Test
	void testNotFoundShouldReturn404() {
		final var uuid = UUID.randomUUID().toString();

		when(mockService.getMandateDetails(MUNICIPALITY_ID, NAMESPACE, uuid)).thenThrow(Problem.valueOf(NOT_FOUND));

		webTestClient.get()
			.uri(uriBuilder -> uriBuilder.path(BASE_URL)
				.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "id", uuid)))
			.exchange()
			.expectStatus().isNotFound()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class);

		verify(mockService).getMandateDetails(MUNICIPALITY_ID, NAMESPACE, uuid);
	}
}
