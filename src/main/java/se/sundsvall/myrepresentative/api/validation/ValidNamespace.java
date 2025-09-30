package se.sundsvall.myrepresentative.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidNamespaceValidator.class)
@Documented
public @interface ValidNamespace {
	String message() default "Invalid namespace, valid characters are a-z, A-Z, 0-9, '-', '_' and '.'";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
