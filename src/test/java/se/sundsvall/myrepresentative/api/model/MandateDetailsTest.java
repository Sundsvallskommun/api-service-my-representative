package se.sundsvall.myrepresentative.api.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MandateDetailsTest {

	private static final String ID = UUID.randomUUID().toString();
	private static final GrantorDetails GRANTOR_DETAILS = GrantorDetailsBuilder.create().build();
	private static final List<GranteeDetails> GRANTEE_DETAILS = List.of(GranteeDetailsBuilder.create().build());
	private static final OffsetDateTime CREATED = OffsetDateTime.now();
	private static final OffsetDateTime VALID_FROM = CREATED.plusDays(2);
	private static final OffsetDateTime VALID_TO = VALID_FROM.plusDays(30);
	private static final String STATUS = MandateStatus.ACTIVE.name();

	@Test
	void testConstructor() {
		final var mandateDetails = new MandateDetails(ID, GRANTOR_DETAILS, GRANTEE_DETAILS, CREATED, VALID_FROM, VALID_TO, STATUS);
		assertBean(mandateDetails);
	}

	@Test
	void testBuilder() {
		final var mandateDetails = MandateDetailsBuilder.create()
			.withId(ID)
			.withGrantorDetails(GRANTOR_DETAILS)
			.withGranteeDetails(GRANTEE_DETAILS)
			.withCreated(CREATED)
			.withValidFrom(VALID_FROM)
			.withValidTo(VALID_TO)
			.withStatus(STATUS)
			.build();

		assertBean(mandateDetails);
	}

	@Test
	void noDirtOnEmptyBean() {
		assertThat(new MandateDetails(null, null, null, null, null, null, null)).hasAllNullFieldsOrProperties();
		assertThat(MandateDetailsBuilder.create().build()).hasAllNullFieldsOrProperties();
	}

	private void assertBean(MandateDetails mandateDetails) {
		assertThat(mandateDetails.id()).isEqualTo(ID);
		assertThat(mandateDetails.grantorDetails()).isEqualTo(GRANTOR_DETAILS);
		assertThat(mandateDetails.granteeDetails()).isEqualTo(GRANTEE_DETAILS);
		assertThat(mandateDetails.created()).isEqualTo(CREATED);
		assertThat(mandateDetails.validFrom()).isEqualTo(VALID_FROM);
		assertThat(mandateDetails.validTo()).isEqualTo(VALID_TO);
		assertThat(mandateDetails.status()).isEqualTo(STATUS);

		assertThat(mandateDetails).hasNoNullFieldsOrProperties();
	}
}
