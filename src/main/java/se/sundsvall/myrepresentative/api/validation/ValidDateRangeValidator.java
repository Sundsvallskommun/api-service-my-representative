package se.sundsvall.myrepresentative.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ValidDateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

	private String fromField;
	private String toField;

	@Override
	public void initialize(ValidDateRange constraintAnnotation) {
		this.fromField = constraintAnnotation.activeFromField();
		this.toField = constraintAnnotation.inactiveAfterField();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		try {
			final var activeFromField = value.getClass().getDeclaredField(this.fromField);
			final var inactiveAfterField = value.getClass().getDeclaredField(this.toField);

			activeFromField.setAccessible(true);
			inactiveAfterField.setAccessible(true);

			final var activeFrom = (LocalDate) activeFromField.get(value);
			final var inactiveAfter = (LocalDate) inactiveAfterField.get(value);

			if (activeFrom == null || inactiveAfter == null) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("Both activeFrom and inactiveAfter must be provided").addConstraintViolation();
				return false;
			}

			if (activeFrom.isAfter(inactiveAfter)) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("activeFrom cannot be after invalidAfter").addConstraintViolation();
				return false;
			}

			if (inactiveAfter.isBefore(LocalDate.now())) {
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate("inactiveAfter must be today or later").addConstraintViolation();
				return false;

			}

			return true;

		} catch (Exception e) {
			return false;
		}
	}
}
