package se.sundsvall.myrepresentative.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;
import static se.sundsvall.myrepresentative.TestObjectFactory.createMandate;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;
import se.sundsvall.myrepresentative.service.RepresentativesService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class MunicipalityIdFailureTest {

	private static final String BASE_URL = "/{municipalityId}/{namespace}/mandates";
	private static final String NOT_A_VALID_MUNICIPALITY_ID = "not a valid municipality ID";

	@MockitoBean
	private RepresentativesService mockService;

	@Autowired
	private WebTestClient webTestClient;

	@ParameterizedTest(name = "{0}")
	@MethodSource("invalidMunicipalityIdProvider")
	void faultyMunicipalityIdShouldReturn400(String description, String method, String field) {
		final var invalidMunicipalityId = "invalid-municipality";
		final var validId = "550e8400-e29b-41d4-a716-446655440000";

		switch (method) {
			case "POST" -> {
				final var responseSpec = webTestClient.post()
					.uri(BASE_URL, invalidMunicipalityId, NAMESPACE)
					.bodyValue(createMandate())
					.exchange();

				assertViolation(responseSpec, field);
			}

			case "GET_SEARCH" -> {
				final var responseSpec = webTestClient.get()
					.uri(BASE_URL, invalidMunicipalityId, NAMESPACE)
					.exchange();

				assertViolation(responseSpec, field);
			}

			case "GET_BY_ID" -> {
				final var responseSpec = webTestClient.get()
					.uri(BASE_URL + "/{id}", invalidMunicipalityId, NAMESPACE, validId)
					.exchange();

				assertViolation(responseSpec, field);
			}

			case "DELETE" -> {
				final var responseSpec = webTestClient.delete()
					.uri(BASE_URL + "/{id}", invalidMunicipalityId, NAMESPACE, validId)
					.exchange();

				assertViolation(responseSpec, field);
			}

			default -> throw new IllegalArgumentException("Unsupported HTTP method: " + method);
		}

		verifyNoInteractions(mockService);
	}

	private void assertViolation(WebTestClient.ResponseSpec spec, final String field) {
		final var problem = spec.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(problem.getViolations())
			.extracting(Violation::getField, Violation::getMessage)
			.containsExactly(tuple(field, NOT_A_VALID_MUNICIPALITY_ID));
	}

	private static Stream<Arguments> invalidMunicipalityIdProvider() {
		return Stream.of(
			Arguments.of("POST /mandates", "POST", "createMandate.municipalityId"),
			Arguments.of("GET /mandates", "GET_SEARCH", "searchMandates.municipalityId"),
			Arguments.of("GET /mandates/{id}", "GET_BY_ID", "getMandateById.municipalityId"),
			Arguments.of("DELETE /mandates/{id}", "DELETE", "deleteMandate.municipalityId"));
	}
}
