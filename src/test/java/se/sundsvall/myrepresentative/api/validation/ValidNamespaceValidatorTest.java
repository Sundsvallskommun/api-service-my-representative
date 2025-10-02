package se.sundsvall.myrepresentative.api.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;

import jakarta.validation.ConstraintValidatorContext;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

class ValidNamespaceValidatorTest {

	@ParameterizedTest(name = "{0}")
	@MethodSource("namespaceProvider")
	void testValidNamespaces(String testName, String namespace, boolean expectedIsValid) {
		var mockContext = Mockito.mock(ConstraintValidatorContext.class);
		ValidNamespaceValidator validator = new ValidNamespaceValidator();

		assertThat(validator.isValid(namespace, mockContext)).isEqualTo(expectedIsValid);

		verifyNoInteractions(mockContext);
	}

	public static Stream<Arguments> namespaceProvider() {
		return Stream.of(
			Arguments.of("all valid characters", "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-._", true),
			Arguments.of("max length is ok", "a".repeat(128), true),
			Arguments.of("one character too long", "a".repeat(129), false),
			Arguments.of("invalid characters", "invalid!namespace", false),
			Arguments.of("empty string", "", false),
			Arguments.of("blank string", "   ", false),
			Arguments.of("null value", null, false));
	}
}
