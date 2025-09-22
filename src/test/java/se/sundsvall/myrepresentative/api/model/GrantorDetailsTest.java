package se.sundsvall.myrepresentative.api.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class GrantorDetailsTest {

	private static final String GRANTOR_PARTY_ID = UUID.randomUUID().toString();
	private static final String SIGNATORY_PARTY_ID = UUID.randomUUID().toString();
	private static final String NAME = "Ankeborgs Margarinfabrik";

	@Test
	void testConstructor() {
		var grantorDetails = new GrantorDetails(NAME, GRANTOR_PARTY_ID, SIGNATORY_PARTY_ID);
		assertBean(grantorDetails);
	}

	@Test
	void testBuilder() {
		final var grantorDetails = GrantorDetailsBuilder.create()
			.withName(NAME)
			.withGrantorPartyId(GRANTOR_PARTY_ID)
			.withSignatoryPartyId(SIGNATORY_PARTY_ID)
			.build();

		assertBean(grantorDetails);
	}

	@Test
	void noDirtOnEmptyBean() {
		assertThat(new GrantorDetails(null, null, null)).hasAllNullFieldsOrProperties();
		assertThat(GrantorDetailsBuilder.create().build()).hasAllNullFieldsOrProperties();
	}

	private static void assertBean(GrantorDetails details) {
		assertThat(details.name()).isEqualTo(NAME);
		assertThat(details.grantorPartyId()).isEqualTo(GRANTOR_PARTY_ID);
		assertThat(details.signatoryPartyId()).isEqualTo(SIGNATORY_PARTY_ID);

		assertThat(details).hasNoNullFieldsOrProperties();
	}
}
