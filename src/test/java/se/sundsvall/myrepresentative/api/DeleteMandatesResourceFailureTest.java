package se.sundsvall.myrepresentative.api;

import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import org.zalando.problem.violations.ConstraintViolationProblem;
import org.zalando.problem.violations.Violation;
import se.sundsvall.myrepresentative.service.RepresentativesService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.zalando.problem.Status.BAD_REQUEST;
import static org.zalando.problem.Status.CONFLICT;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;
import static se.sundsvall.myrepresentative.TestObjectFactory.createMandate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class DeleteMandatesResourceFailureTest {

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
	void testDeleteMandateWithFaultyId() {
		final var createMandate = createMandate();
		when(mockService.createMandate(MUNICIPALITY_ID, NAMESPACE, createMandate)).thenThrow(Problem.valueOf(CONFLICT));

		var problem = webTestClient.delete()
			.uri(uriBuilder -> uriBuilder.path(BASE_URL)
				.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE, "id", "faulty-id")))
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
				.extracting(Violation::getField, Violation::getMessage)
				.containsExactlyInAnyOrder(
					Assertions.tuple("deleteMandate.id", "not a valid UUID"));
		});
	}
}
