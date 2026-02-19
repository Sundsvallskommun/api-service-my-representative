package se.sundsvall.myrepresentative.config;

import java.util.Set;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.spring.web.advice.ProblemHandling;

import static org.springframework.http.HttpStatus.CONFLICT;

/**
 * Handle duplicate entries in the database and return a Problem response with 409 CONFLICT.
 * We check for specific error codes errors that indicate a duplicate key violation.
 * If it's a different kind of DataIntegrityViolationException, we return a generic 409 CONFLICT.
 */
@ControllerAdvice
public class DataIntegrityExceptionHandler implements ProblemHandling {

	// Error 1062: Duplicate entry for key
	// Error 1586: Duplicate entry for key name specified
	private static final Set<Integer> DUPLICATE_ENTRY_ERROR_CODES = Set.of(1062, 1586);

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Problem> handleDuplicateEntry(DataIntegrityViolationException ex) {
		var cause = ex.getCause();

		while (cause != null) {
			if (cause instanceof final ConstraintViolationException cve) {
				int errorCode = cve.getErrorCode();
				// Check if the error code indicates a duplicate entry
				if (DUPLICATE_ENTRY_ERROR_CODES.contains(errorCode)) {
					return handleException("Duplicate entry", "A record with the same unique identifier(s) already exists");
				}
			}
			cause = cause.getCause();
		}
		// Fallback for other integrity violations
		return handleException("Data integrity violation", "A data integrity violation occurred");
	}

	private ResponseEntity<Problem> handleException(String title, String detail) {
		return ResponseEntity.status(CONFLICT)
			.body(Problem.builder()
				.withTitle(title)
				.withStatus(Status.CONFLICT)
				.withDetail(detail)
				.build());
	}
}
