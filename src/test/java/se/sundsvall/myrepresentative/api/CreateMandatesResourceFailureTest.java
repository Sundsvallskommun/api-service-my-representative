package se.sundsvall.myrepresentative.api;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.zalando.problem.Status.CONFLICT;
import static org.zalando.problem.Status.FORBIDDEN;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;
import static se.sundsvall.myrepresentative.TestObjectFactory.createMandate;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.zalando.problem.Problem;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.CreateMandateBuilder;
import se.sundsvall.myrepresentative.api.model.GranteeDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.GrantorDetailsBuilder;
import se.sundsvall.myrepresentative.service.RepresentativesService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("junit")
class CreateMandatesResourceFailureTest {

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
	void testCreateMandateShouldThrowConflictWhenMandateAlreadyExists() {
		final var createMandate = createMandate();
		when(mockService.createMandate(MUNICIPALITY_ID, NAMESPACE, createMandate)).thenThrow(Problem.valueOf(CONFLICT));

		webTestClient.post()
			.uri(uriBuilder -> uriBuilder.path(BASE_URL)
				.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE)))
			.bodyValue(createMandate)
			.exchange()
			.expectStatus().isEqualTo(CONFLICT.getStatusCode());

		verify(mockService).createMandate(MUNICIPALITY_ID, NAMESPACE, createMandate);
	}

	@Test
	void testCreateMandateShouldThrowForbiddenWhenSignatoryIsNotAuthorized() {
		final var createMandate = createMandate();
		when(mockService.createMandate(MUNICIPALITY_ID, NAMESPACE, createMandate)).thenThrow(Problem.valueOf(FORBIDDEN));

		webTestClient.post()
			.uri(uriBuilder -> uriBuilder.path(BASE_URL)
				.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE)))
			.bodyValue(createMandate)
			.exchange()
			.expectStatus().isEqualTo(FORBIDDEN.getStatusCode());

		verify(mockService).createMandate(MUNICIPALITY_ID, NAMESPACE, createMandate);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("invalidRequestProvider")
	void faultyRequestParameters(final String testName, final CreateMandate createMandate) {
		webTestClient.post()
			.uri(uriBuilder -> uriBuilder.path(BASE_URL)
				.build(Map.of("municipalityId", MUNICIPALITY_ID, "namespace", NAMESPACE)))
			.bodyValue(createMandate)
			.exchange()
			.expectStatus().isBadRequest();

		verifyNoInteractions(mockService);
	}

	public static Stream<Arguments> invalidRequestProvider() {
		final var now = LocalDate.now();
		final var invalidUuid = "invalid-uuid";

		return Stream.of(
			Arguments.of("invalid grantorPartyId", CreateMandateBuilder.from(createMandate()).withGrantorDetails(GrantorDetailsBuilder.create().withGrantorPartyId(invalidUuid).build()).build()),
			Arguments.of("invalid signatoryPartyId", CreateMandateBuilder.from(createMandate()).withGrantorDetails(GrantorDetailsBuilder.create().withSignatoryPartyId(invalidUuid).build()).build()),
			Arguments.of("invalid granteePartyId", CreateMandateBuilder.from(createMandate()).withGranteeDetails(GranteeDetailsBuilder.create().withPartyId(invalidUuid).build()).build()),
			Arguments.of("activeFrom is after incativeAfter", createMandateWithFaultyDates(now.plusWeeks(1), now)),
			Arguments.of("incativeAfter has passed", createMandateWithFaultyDates(now.minusWeeks(2), now.minusWeeks(1))));
	}

	private static CreateMandate createMandateWithFaultyDates(final LocalDate activeFrom, final LocalDate incativeAfter) {
		return CreateMandateBuilder.from(createMandate()).withActiveFrom(activeFrom).withInactiveAfter(incativeAfter).build();
	}
}
