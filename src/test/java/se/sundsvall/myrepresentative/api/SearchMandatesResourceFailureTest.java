package se.sundsvall.myrepresentative.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.zalando.problem.Status.BAD_REQUEST;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;

import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
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
class SearchMandatesResourceFailureTest {

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
	void testFaultyPartyIds() {
		var problem = webTestClient.get()
			.uri(uriBuilder -> uriBuilder.path(BASE_URL)
				.queryParam("grantorPartyId", "invalid")
				.queryParam("signatoryPartyId", "alsoInvalid")
				.queryParam("granteePartyId", "invalidToo")
				.queryParam("page", 1)
				.queryParam("limit", 10)
				.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE)))
			.exchange()
			.expectStatus().isBadRequest()
			.expectHeader().contentType(APPLICATION_PROBLEM_JSON)
			.expectBody(ConstraintViolationProblem.class)
			.returnResult()
			.getResponseBody();

		assertThat(problem).isNotNull().satisfies(p -> {
			assertThat(p.getTitle()).isEqualTo("Constraint Violation");
			assertThat(p.getStatus()).isEqualTo(BAD_REQUEST);
			assertThat(p.getViolations()).extracting(Violation::getField, Violation::getMessage)
				.containsExactlyInAnyOrder(
					tuple("grantorPartyId", "not a valid UUID"),
					tuple("granteePartyId", "not a valid UUID"),
					tuple("signatoryPartyId", "not a valid UUID"));
		});

		verifyNoInteractions(mockService);
	}
}
