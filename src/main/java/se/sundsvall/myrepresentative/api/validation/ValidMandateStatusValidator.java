package se.sundsvall.myrepresentative.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import se.sundsvall.myrepresentative.api.model.MandateStatus;

public class ValidMandateStatusValidator implements ConstraintValidator<ValidMandateStatus, List<String>> {

	private boolean nullable;

	@Override
	public void initialize(final ValidMandateStatus constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
		this.nullable = constraintAnnotation.nullable();
	}

	@Override
	public boolean isValid(final List<String> value, final ConstraintValidatorContext context) {
		if (value == null) {
			return this.nullable;
		}

		return value.stream()
			.filter(Objects::nonNull)
			.allMatch(predicate -> Arrays.stream(MandateStatus.values())
				.anyMatch(status -> status.name().equalsIgnoreCase(predicate)));
	}

}
