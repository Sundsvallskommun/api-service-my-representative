package se.sundsvall.myrepresentative.integration.legalentity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.createPersonEngagement;

import generated.se.sundsvall.legalentity.PersonEngagement;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.ThrowableProblem;

@ExtendWith(MockitoExtension.class)
class LegalEntityIntegrationTest {

	@Mock
	private LegalEntityClient mockClient;

	@InjectMocks
	private LegalEntityIntegration integration;

	@AfterEach
	void tearDown() {
		verifyNoMoreInteractions(mockClient);
	}

	@Test
	void testGetPersonEngagements() {
		final var personNumber = "personNumber";
		final var personEngagement = createPersonEngagement();
		when(mockClient.getPersonEngagements(MUNICIPALITY_ID, personNumber)).thenReturn(List.of(personEngagement));

		final var personEngagements = integration.getPersonEngagements(MUNICIPALITY_ID, personNumber);

		assertThat(personEngagements).containsExactly(personEngagement);

		verify(mockClient).getPersonEngagements(MUNICIPALITY_ID, personNumber);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("engagementsProvider")
	void testGetPersonEngagementsWhenEmptyResponse_shouldThrowException(final String testName, final List<PersonEngagement> engagements) {
		final var personNumber = "personNumber";
		when(mockClient.getPersonEngagements(MUNICIPALITY_ID, personNumber)).thenReturn(engagements);

		assertThatExceptionOfType(ThrowableProblem.class).isThrownBy(() -> integration.getPersonEngagements(MUNICIPALITY_ID, personNumber))
			.satisfies(problem -> {
				assertThat(problem.getStatus()).isEqualTo(NOT_FOUND);
				assertThat(problem.getTitle()).isEqualTo("No engagements found");
				assertThat(problem.getDetail()).isEqualTo("No matching engagements found for the given person");
			});

		verify(mockClient).getPersonEngagements(MUNICIPALITY_ID, personNumber);
	}

	private static Stream<Arguments> engagementsProvider() {
		return Stream.of(
			Arguments.of("null list", null),
			Arguments.of("empty list", List.of()));
	}
}
