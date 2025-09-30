package se.sundsvall.myrepresentative.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.stream.Stream;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

class DataIntegrityExceptionHandlerTest {

	private final DataIntegrityExceptionHandler handler = new DataIntegrityExceptionHandler();
	private static final String DUPLICATE_ENTRY_TITLE = "Duplicate entry";
	private static final String DUPLICATE_ENTRY_MESSAGE = "A record with the same unique identifier(s) already exists";
	private static final String GENERIC_DUPLICATE_ENTRY_TITLE = "Data integrity violation";
	private static final String GENERIC_DUPLICATE_ENTRY_MESSAGE = "A data integrity violation occurred";

	public static Stream<Arguments> duplcateEntryProvider() {
		return Stream.of(
			Arguments.of("error 1062", 1062, DUPLICATE_ENTRY_TITLE, DUPLICATE_ENTRY_MESSAGE),
			Arguments.of("error 1586", 1586, DUPLICATE_ENTRY_TITLE, DUPLICATE_ENTRY_MESSAGE),
			Arguments.of("unknown error", 9999, GENERIC_DUPLICATE_ENTRY_TITLE, GENERIC_DUPLICATE_ENTRY_MESSAGE));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("duplcateEntryProvider")
	void handleDuplicateEntry_shouldReturnProblem(final String testName, final int errorCode, final String expectedTitle, final String expectedMessage) {
		final var sqlIntegrityException = new SQLIntegrityConstraintViolationException("Duplicate entry", "23000", errorCode);
		final var constraintViolationException = new ConstraintViolationException("Constraint violation", sqlIntegrityException, "some_constraint");
		final var dataIntegrityViolationException = new DataIntegrityViolationException("Data integrity violation", constraintViolationException);

		final var responseEntity = handler.handleDuplicateEntry(dataIntegrityViolationException);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(responseEntity.getBody())
			.isNotNull()
			.extracting(Problem::getTitle, Problem::getStatus, Problem::getDetail)
			.containsExactly(expectedTitle, Status.CONFLICT, expectedMessage);
	}

	@Test
	void handleDuplicateEntry_withNonConstraintViolationException_shouldReturnGenericProblem() {
		final var dataIntegrityViolationException = new DataIntegrityViolationException("Data integrity violation without constraint");

		final var responseEntity = handler.handleDuplicateEntry(dataIntegrityViolationException);

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
		assertThat(responseEntity.getBody())
			.isNotNull()
			.extracting(Problem::getTitle, Problem::getStatus, Problem::getDetail)
			.containsExactly(GENERIC_DUPLICATE_ENTRY_TITLE, Status.CONFLICT, GENERIC_DUPLICATE_ENTRY_MESSAGE);
	}
}
