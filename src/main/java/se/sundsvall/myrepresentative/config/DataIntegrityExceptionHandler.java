package se.sundsvall.myrepresentative.config;

import static org.springframework.http.HttpStatus.CONFLICT;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;

/**
 * Handle duplicate entries in the database and return a proper Problem response.
 */
@ControllerAdvice
public class DataIntegrityExceptionHandler implements ProblemHandling {

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Problem> handleDuplicateEntry(DataIntegrityViolationException ex) {
		return ResponseEntity.status(CONFLICT)
			.body(Problem.builder()
				.withTitle("Duplicate entry")
				.withStatus(Status.CONFLICT)
				.withDetail("A record with the same unique identifier(s) already exists.")
				.build());
	}
}
