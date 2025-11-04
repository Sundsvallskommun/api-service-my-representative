package se.sundsvall.myrepresentative.api.validation;

import static java.lang.annotation.ElementType.FIELD;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidMandateStatusValidator.class)
@Documented
public @interface ValidMandateStatus {

	boolean nullable() default false;

	String message() default "Invalid namespace, valid characters are a-z, A-Z, 0-9, '-', '_' and '.'";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
