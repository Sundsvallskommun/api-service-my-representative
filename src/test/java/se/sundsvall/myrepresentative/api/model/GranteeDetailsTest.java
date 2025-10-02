package se.sundsvall.myrepresentative.api.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class GranteeDetailsTest {

	private static final String PARTY_ID = UUID.randomUUID().toString();

	@Test
	void testConstructor() {
		final var granteeDetails = new GranteeDetails(PARTY_ID);
		assertBean(granteeDetails);
	}

	@Test
	void testBuilder() {
		final var granteeDetails = GranteeDetailsBuilder.create()
			.withPartyId(PARTY_ID)
			.build();

		assertBean(granteeDetails);
	}

	@Test
	void noDirtOnEmptyBean() {
		assertThat(new GranteeDetails(null)).hasAllNullFieldsOrProperties();
		assertThat(GranteeDetailsBuilder.create().build()).hasAllNullFieldsOrProperties();
	}

	private static void assertBean(GranteeDetails details) {
		assertThat(details.partyId()).isEqualTo(PARTY_ID);

		assertThat(details).hasNoNullFieldsOrProperties();
	}
}
