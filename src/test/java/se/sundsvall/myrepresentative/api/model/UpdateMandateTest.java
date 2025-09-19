package se.sundsvall.myrepresentative.api.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UpdateMandateTest {

	private static final String ID = UUID.randomUUID().toString();
	private static final List<GranteeDetails> GRANTEE_DETAILS = List.of(GranteeDetailsBuilder.create().build());
	private static final OffsetDateTime VALID_FROM = OffsetDateTime.now();
	private static final OffsetDateTime VALID_TO = VALID_FROM.plusDays(30);

	@Test
	void testConstructor() {
		final var updateMandate = new UpdateMandate(ID, GRANTEE_DETAILS, VALID_FROM, VALID_TO);
		assertBean(updateMandate);
	}

	@Test
	void testBuilder() {
		final var updateMandate = UpdateMandateBuilder.create()
			.withId(ID)
			.withGranteeDetails(GRANTEE_DETAILS)
			.withValidFrom(VALID_FROM)
			.withValidTo(VALID_TO)
			.build();

		assertBean(updateMandate);
	}

	@Test
	void noDirtOnEmptyBean() {
		assertThat(new UpdateMandate(null, null, null, null)).hasAllNullFieldsOrProperties();
		assertThat(UpdateMandateBuilder.create().build()).hasAllNullFieldsOrProperties();
	}

	private void assertBean(UpdateMandate updateMandate) {
		assertThat(updateMandate.id()).isEqualTo(ID);
		assertThat(updateMandate.granteeDetails()).isEqualTo(GRANTEE_DETAILS);
		assertThat(updateMandate.validFrom()).isEqualTo(VALID_FROM);
		assertThat(updateMandate.validTo()).isEqualTo(VALID_TO);

		assertThat(updateMandate).hasNoNullFieldsOrProperties();
	}
}
