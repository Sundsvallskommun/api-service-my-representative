package se.sundsvall.myrepresentative.api.model;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.myrepresentative.TestObjectFactory.ACTIVE_FROM;
import static se.sundsvall.myrepresentative.TestObjectFactory.CREATED;
import static se.sundsvall.myrepresentative.TestObjectFactory.ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.INACTIVE_AFTER;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;
import static se.sundsvall.myrepresentative.TestObjectFactory.STATUS;
import static se.sundsvall.myrepresentative.TestObjectFactory.UPDATED;
import static se.sundsvall.myrepresentative.TestObjectFactory.createSigningInfo;

import org.junit.jupiter.api.Test;

class MandateDetailsTest {

	private static final GrantorDetails GRANTOR_DETAILS = GrantorDetailsBuilder.create().build();
	private static final GranteeDetails GRANTEE_DETAILS = GranteeDetailsBuilder.create().build();
	private static final SigningInfo SIGNING_INFO = createSigningInfo();
	private static final boolean WHITELISTED = true;

	@Test
	void testConstructor() {
		final var mandateDetails = new MandateDetails(ID, GRANTOR_DETAILS, GRANTEE_DETAILS, MUNICIPALITY_ID, NAMESPACE, CREATED, UPDATED, ACTIVE_FROM, INACTIVE_AFTER, STATUS, SIGNING_INFO, WHITELISTED);
		assertBean(mandateDetails);
	}

	@Test
	void testBuilder() {
		final var mandateDetails = MandateDetailsBuilder.create()
			.withId(ID)
			.withGrantorDetails(GRANTOR_DETAILS)
			.withGranteeDetails(GRANTEE_DETAILS)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withCreated(CREATED)
			.withUpdated(UPDATED)
			.withActiveFrom(ACTIVE_FROM)
			.withInactiveAfter(INACTIVE_AFTER)
			.withStatus(STATUS)
			.withSigningInfo(SIGNING_INFO)
			.withWhitelisted(WHITELISTED)
			.build();

		assertBean(mandateDetails);
	}

	@Test
	void noDirtOnEmptyBean() {
		assertThat(new MandateDetails(null, null, null, null, null, null, null, null, null, null, null, false)).hasAllNullFieldsOrPropertiesExcept("whitelisted");
		assertThat(MandateDetailsBuilder.create().build()).hasAllNullFieldsOrPropertiesExcept("whitelisted");
	}

	private void assertBean(MandateDetails mandateDetails) {
		assertThat(mandateDetails.id()).isEqualTo(ID);
		assertThat(mandateDetails.grantorDetails()).isEqualTo(GRANTOR_DETAILS);
		assertThat(mandateDetails.granteeDetails()).isEqualTo(GRANTEE_DETAILS);
		assertThat(mandateDetails.municipalityId()).isEqualTo(MUNICIPALITY_ID);
		assertThat(mandateDetails.namespace()).isEqualTo(NAMESPACE);
		assertThat(mandateDetails.created()).isEqualTo(CREATED);
		assertThat(mandateDetails.updated()).isEqualTo(UPDATED);
		assertThat(mandateDetails.activeFrom()).isEqualTo(ACTIVE_FROM);
		assertThat(mandateDetails.inactiveAfter()).isEqualTo(INACTIVE_AFTER);
		assertThat(mandateDetails.status()).isEqualTo(STATUS);
		assertThat(mandateDetails.signingInfo()).isEqualTo(SIGNING_INFO);
		assertThat(mandateDetails.whitelisted()).isEqualTo(WHITELISTED);

		assertThat(mandateDetails).hasNoNullFieldsOrProperties();
	}
}
