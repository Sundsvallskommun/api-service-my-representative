package se.sundsvall.myrepresentative.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.createMandate;

import java.util.UUID;
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
class NamespaceFailureTest {

	private static final String BASE_URL = "/{municipalityId}/{namespace}/mandates";
	private static final String NOT_A_VALID_NAMESPACE = "Invalid namespace, valid characters are a-z, A-Z, 0-9, '-', '_' and '.'";

	@MockitoBean
	private RepresentativesService mockService;

	@Autowired
	private WebTestClient webTestClient;

	@ParameterizedTest(name = "{0}")
	@MethodSource("invalidNamespaceProvider")
	void faultyMunicipalityIdShouldReturn400(String description, String method, String field) {
		final var invalidNamespace = "not!|valid";
		final var validId = UUID.randomUUID().toString();

		switch (method) {
			case "POST" -> {
				final var responseSpec = webTestClient.post()
					.uri(BASE_URL, MUNICIPALITY_ID, invalidNamespace)
					.bodyValue(createMandate())
					.exchange();

				assertViolation(responseSpec, field);
			}

			case "GET_SEARCH" -> {
				final var responseSpec = webTestClient.get()
					.uri(BASE_URL, MUNICIPALITY_ID, invalidNamespace)
					.exchange();

				assertViolation(responseSpec, field);
			}

			case "GET_BY_ID" -> {
				final var responseSpec = webTestClient.get()
					.uri(BASE_URL + "/{id}", MUNICIPALITY_ID, invalidNamespace, validId)
					.exchange();

				assertViolation(responseSpec, field);
			}

			case "DELETE" -> {
				final var responseSpec = webTestClient.delete()
					.uri(BASE_URL + "/{id}", MUNICIPALITY_ID, invalidNamespace, validId)
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
			.containsExactly(tuple(field, NOT_A_VALID_NAMESPACE));
	}

	private static Stream<Arguments> invalidNamespaceProvider() {
		return Stream.of(
			Arguments.of("POST /mandates", "POST", "createMandate.namespace"),
			Arguments.of("GET /mandates", "GET_SEARCH", "searchMandates.namespace"),
			Arguments.of("GET /mandates/{id}", "GET_BY_ID", "getMandateById.namespace"),
			Arguments.of("DELETE /mandates/{id}", "DELETE", "deleteMandate.namespace"));
	}
}
