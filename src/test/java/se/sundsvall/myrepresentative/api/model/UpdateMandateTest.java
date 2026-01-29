package se.sundsvall.myrepresentative.api.model;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.myrepresentative.TestObjectFactory.ACTIVE_FROM;
import static se.sundsvall.myrepresentative.TestObjectFactory.INACTIVE_AFTER;

import org.junit.jupiter.api.Test;

class UpdateMandateTest {

	private static final GranteeDetails GRANTEE_DETAILS = GranteeDetailsBuilder.create().build();

	@Test
	void testConstructor() {
		final var updateMandate = new UpdateMandate(GRANTEE_DETAILS, ACTIVE_FROM, INACTIVE_AFTER);
		assertBean(updateMandate);
	}

	@Test
	void testBuilder() {
		final var updateMandate = UpdateMandateBuilder.create()
			.withGranteeDetails(GRANTEE_DETAILS)
			.withActiveFrom(ACTIVE_FROM)
			.withInactiveAfter(INACTIVE_AFTER)
			.build();

		assertBean(updateMandate);
	}

	@Test
	void noDirtOnEmptyBean() {
		assertThat(new UpdateMandate(null, null, null)).hasAllNullFieldsOrProperties();
		assertThat(UpdateMandateBuilder.create().build()).hasAllNullFieldsOrProperties();
	}

	private void assertBean(UpdateMandate updateMandate) {
		assertThat(updateMandate.granteeDetails()).isEqualTo(GRANTEE_DETAILS);
		assertThat(updateMandate.activeFrom()).isEqualTo(ACTIVE_FROM);
		assertThat(updateMandate.inactiveAfter()).isEqualTo(INACTIVE_AFTER);

		assertThat(updateMandate).hasNoNullFieldsOrProperties();
	}
}
