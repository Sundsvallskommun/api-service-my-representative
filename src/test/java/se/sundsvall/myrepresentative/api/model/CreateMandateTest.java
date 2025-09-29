package se.sundsvall.myrepresentative.api.model;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.myrepresentative.TestObjectFactory.ACTIVE_FROM;
import static se.sundsvall.myrepresentative.TestObjectFactory.INACTIVE_AFTER;

import org.junit.jupiter.api.Test;

class CreateMandateTest {

	private static final GrantorDetails GRANTOR_DETAILS = GrantorDetailsBuilder.create().build();
	private static final GranteeDetails GRANTEE_DETAILS = GranteeDetailsBuilder.create().build();

	@Test
	void testConstructor() {
		final var mandate = new CreateMandate(GRANTOR_DETAILS, GRANTEE_DETAILS, ACTIVE_FROM, INACTIVE_AFTER);
		assertBean(mandate);
	}

	@Test
	void testBuilder() {
		final var mandate = CreateMandateBuilder.create()
			.withGrantorDetails(GRANTOR_DETAILS)
			.withGranteeDetails(GRANTEE_DETAILS)
			.withActiveFrom(ACTIVE_FROM)
			.withInactiveAfter(INACTIVE_AFTER)
			.build();

		assertBean(mandate);
	}

	private static void assertBean(CreateMandate mandate) {
		assertThat(mandate.granteeDetails()).isEqualTo(GRANTEE_DETAILS);
		assertThat(mandate.grantorDetails()).isEqualTo(GRANTOR_DETAILS);
		assertThat(mandate.activeFrom()).isEqualTo(ACTIVE_FROM);
		assertThat(mandate.inactiveAfter()).isEqualTo(INACTIVE_AFTER);

		assertThat(mandate).hasNoNullFieldsOrProperties();
	}
}
