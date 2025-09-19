package se.sundsvall.myrepresentative.api.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class CreateMandateTest {

	private static final GrantorDetails GRANTOR_DETAILS = GrantorDetailsBuilder.create().build();
	private static final List<GranteeDetails> GRANTEE_DETAILS = List.of(GranteeDetailsBuilder.create().build());
	private static final OffsetDateTime VALID_FROM = OffsetDateTime.now();
	private static final OffsetDateTime VALID_TO = VALID_FROM.plusDays(30);

	@Test
	void testConstructor() {
		final var mandate = new CreateMandate(GRANTOR_DETAILS, GRANTEE_DETAILS, VALID_FROM, VALID_TO);
		assertBean(mandate);
	}

	@Test
	void testBuilder() {
		final var mandate = CreateMandateBuilder.create()
			.withGrantorDetails(GRANTOR_DETAILS)
			.withGranteeDetails(GRANTEE_DETAILS)
			.withValidFrom(VALID_FROM)
			.withValidTo(VALID_TO)
			.build();

		assertBean(mandate);
	}

	@Test
	void noDirtOnEmptyBean() {
		assertThat(new CreateMandate(null, null, null, null)).hasAllNullFieldsOrProperties();
		assertThat(CreateMandateBuilder.create().build()).hasAllNullFieldsOrProperties();
	}

	private static void assertBean(CreateMandate mandate) {
		assertThat(mandate.granteeDetails()).isEqualTo(GRANTEE_DETAILS);
		assertThat(mandate.grantorDetails()).isEqualTo(GRANTOR_DETAILS);
		assertThat(mandate.validFrom()).isEqualTo(VALID_FROM);
		assertThat(mandate.validTo()).isEqualTo(VALID_TO);

		assertThat(mandate).hasNoNullFieldsOrProperties();
	}
}
