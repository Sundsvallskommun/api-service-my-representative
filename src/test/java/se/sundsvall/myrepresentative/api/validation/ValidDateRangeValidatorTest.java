package se.sundsvall.myrepresentative.api.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.sundsvall.myrepresentative.TestObjectFactory;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.CreateMandateBuilder;

@ExtendWith(MockitoExtension.class)
class ValidDateRangeValidatorTest {

	private static final String ACTIVE_FROM_AFTER_INVALID_AFTER_MESSAGE = "activeFrom cannot be after invalidAfter";
	private static final String ACTIVE_FROM_MUST_BE_PROVIDED_MESSAGE = "activeFrom must be provided";
	private static final String INACTIVE_AFTER_MUST_BE_TODAY_OR_LATER = "inactiveAfter must be today or later";

	private ValidDateRangeValidator validator;

	@Mock
	private ConstraintValidatorContext mockContext;

	@Mock
	private ConstraintValidatorContext.ConstraintViolationBuilder mockBuilder;

	@BeforeEach
	void setUp() {
		validator = new ValidDateRangeValidator();
		validator.initialize(new DummyValidDateRange());
	}

	@AfterEach
	void tearDown() {
		verifyNoMoreInteractions(mockContext, mockBuilder);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("createFaultyDatesProvider")
	void testFaultyDates(final String testName, CreateMandate createMandate, boolean expected, final String expectedViolationMessage) {
		when(mockContext.buildConstraintViolationWithTemplate(any())).thenReturn(mockBuilder);
		when(mockBuilder.addConstraintViolation()).thenReturn(mockContext);

		assertThat(validator.isValid(createMandate, mockContext)).isEqualTo(expected);
		verify(mockContext).disableDefaultConstraintViolation();
		verify(mockContext).buildConstraintViolationWithTemplate(expectedViolationMessage);
		verify(mockBuilder).addConstraintViolation();
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("createValidDatesProvider")
	void testValidDates(final String testName, CreateMandate createMandate, boolean expected) {
		assertThat(validator.isValid(createMandate, mockContext)).isEqualTo(expected);
		verifyNoInteractions(mockContext, mockBuilder);
	}

	public static Stream<Arguments> createFaultyDatesProvider() {
		final var now = LocalDate.now();
		return Stream.of(
			Arguments.of("activeFrom is after incativeAfter", createMandate(now.plusWeeks(1), now), false, ACTIVE_FROM_AFTER_INVALID_AFTER_MESSAGE),
			Arguments.of("incativeAfter has passed", createMandate(now.minusWeeks(2), now.minusWeeks(1)), false, INACTIVE_AFTER_MUST_BE_TODAY_OR_LATER),
			Arguments.of("activeFrom is null", createMandate(null, now), false, ACTIVE_FROM_MUST_BE_PROVIDED_MESSAGE),
			Arguments.of("both activeFrom and incativeAfter are null", createMandate(null, null), false, ACTIVE_FROM_MUST_BE_PROVIDED_MESSAGE));
	}

	public static Stream<Arguments> createValidDatesProvider() {
		final var now = LocalDate.now();
		return Stream.of(
			Arguments.of("activeFrom is before incativeAfter", createMandate(now, now.plusWeeks(1)), true),
			Arguments.of("future dates are valid", createMandate(now.plusWeeks(1), now.plusWeeks(2)), true),
			Arguments.of("activeFrom has passed but not invalidAfter", createMandate(now.minusWeeks(1), now.plusWeeks(1)), true));
	}

	private static CreateMandate createMandate(LocalDate activeFrom, LocalDate inactiveAfter) {
		final var mandate = TestObjectFactory.createMandate();
		return CreateMandateBuilder.from(mandate)
			.withActiveFrom(activeFrom)
			.withInactiveAfter(inactiveAfter)
			.build();
	}

	private static class DummyValidDateRange implements ValidDateRange {
		@Override
		public String message() {
			return "";
		}

		@Override
		public Class<?>[] groups() {
			return new Class[0];
		}

		@Override
		public Class<? extends Payload>[] payload() {
			return new Class[0];
		}

		public String activeFromField() {
			return "activeFrom";
		}

		public String inactiveAfterField() {
			return "inactiveAfter";
		}

		public Class<? extends java.lang.annotation.Annotation> annotationType() {
			return ValidDateRange.class;
		}
	}
}
