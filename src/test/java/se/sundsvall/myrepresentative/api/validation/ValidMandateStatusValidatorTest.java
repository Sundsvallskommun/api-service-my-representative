package se.sundsvall.myrepresentative.api.validation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidMandateStatusValidatorTest {

	@InjectMocks
	private ValidMandateStatusValidator validator;

	@Test
	void testIsValid() {

		// Act
		final var result = validator.isValid(List.of("Active"), null);

		// Assert
		assertThat(result).isTrue();
	}

	@Test
	void testIsInvalid() {

		// Act
		final var result = validator.isValid(List.of("invalid"), null);

		// Assert
		assertThat(result).isFalse();
	}

	@Test
	void testNull() {

		// Act
		final var result = validator.isValid(null, null);

		// Assert
		assertThat(result).isFalse();
	}

}
