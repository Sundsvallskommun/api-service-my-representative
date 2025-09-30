package se.sundsvall.myrepresentative.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class ValidNamespaceValidator implements ConstraintValidator<ValidNamespace, String> {

	// Only allow letters (a-z, A-Z), digits (0-9), hyphens(-), underscores(_) and dots(.)
	private static final String NAMESPACE_REGEXP = "[\\w\\-\\.]+";
	private static final int MAX_LENGTH = 128;

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isBlank(value) || value.length() > MAX_LENGTH) {
			return false;
		}

		return value.matches(NAMESPACE_REGEXP);
	}
}
