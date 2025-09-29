package se.sundsvall.myrepresentative.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

class DataIntegrityExceptionHandlerTest {

	@Test
	void handleDuplicateEntry_shouldReturnProblem() {
		DataIntegrityExceptionHandler exceptionHandler = new DataIntegrityExceptionHandler();
		DataIntegrityViolationException ex = new DataIntegrityViolationException("Duplicate");

		ResponseEntity<Problem> response = exceptionHandler.handleDuplicateEntry(ex);

		assertThat(response.getStatusCode().value()).isEqualTo(409);
		assertThat(response.getBody().getTitle()).isEqualTo("Duplicate entry");
		assertThat(response.getBody().getStatus()).isEqualTo(Status.CONFLICT);
		assertThat(response.getBody().getDetail()).isEqualTo("A record with the same unique identifier(s) already exists.");
	}
}
