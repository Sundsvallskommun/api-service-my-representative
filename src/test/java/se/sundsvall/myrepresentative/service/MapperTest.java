package se.sundsvall.myrepresentative.service;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.myrepresentative.TestObjectFactory.ACTIVE_FROM;
import static se.sundsvall.myrepresentative.TestObjectFactory.GRANTEE_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.GRANTOR_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.INACTIVE_AFTER;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAME;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;
import static se.sundsvall.myrepresentative.TestObjectFactory.SIGNATORY_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.STATUS;
import static se.sundsvall.myrepresentative.TestObjectFactory.createSigningInfo;
import static se.sundsvall.myrepresentative.TestObjectFactory.createSigningInfoEntity;

import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import se.sundsvall.myrepresentative.api.model.CreateMandateBuilder;
import se.sundsvall.myrepresentative.api.model.GranteeDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.GrantorDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.SigningInfo;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;
import se.sundsvall.myrepresentative.integration.db.entity.SigningInformationEntity;

class MapperTest {

	private final Mapper mapper = new Mapper();
	private static final SigningInfo SIGNING_INFO = createSigningInfo();
	private static final SigningInformationEntity SIGNING_INFORMATION_ENTITY = createSigningInfoEntity();

	@Test
	void testMapToMandateEntity() {
		final var createMandate = CreateMandateBuilder.create()
			.withGrantorDetails(GrantorDetailsBuilder.create()
				.withGrantorPartyId(GRANTOR_PARTY_ID)
				.withSignatoryPartyId(SIGNATORY_PARTY_ID)
				.withName(NAME)
				.build())
			.withGranteeDetails(GranteeDetailsBuilder.create()
				.withPartyId(GRANTEE_PARTY_ID)
				.build())
			.withActiveFrom(ACTIVE_FROM)
			.withInactiveAfter(INACTIVE_AFTER)
			.withSigningInfo(SIGNING_INFO)
			.build();

		final var entity = mapper.toMandateEntity(MUNICIPALITY_ID, NAMESPACE, createMandate);

		assertThat(entity.getName()).isEqualTo(NAME);
		assertThat(entity.getGrantorPartyId()).isEqualTo(GRANTOR_PARTY_ID);
		assertThat(entity.getSignatoryPartyId()).isEqualTo(SIGNATORY_PARTY_ID);
		assertThat(entity.getGranteePartyId()).isEqualTo(GRANTEE_PARTY_ID);
		assertThat(entity.getNamespace()).isEqualTo(NAMESPACE);
		assertThat(entity.getMunicipalityId()).isEqualTo(MUNICIPALITY_ID);
		assertThat(entity.getActiveFrom()).isEqualTo(ACTIVE_FROM);
		assertThat(entity.getInactiveAfter()).isEqualTo(INACTIVE_AFTER);
		assertSigningInformation(entity.getLatestSigningInformation());

		// These shouldn't be set when mapping from API model to entity
		assertThat(entity.getId()).isNull();
		assertThat(entity.getCreated()).isNull();
		assertThat(entity.getUpdated()).isNull();
		assertThat(entity.getDeleted()).isNull();
	}

	private void assertSigningInformation(SigningInformationEntity entity) {
		assertThat(entity.getStatus()).isEqualTo(SIGNING_INFO.status());
		assertThat(entity.getSigned()).isEqualTo(SIGNING_INFO.signed());
		assertThat(entity.getOrderRef()).isEqualTo(SIGNING_INFO.orderRef());
		assertThat(entity.getSignatureData()).isEqualTo(SIGNING_INFO.signature());
		assertThat(entity.getOcspResponse()).isEqualTo(SIGNING_INFO.ocspResponse());
		assertThat(entity.getBankIdIssueDate()).isEqualTo(SIGNING_INFO.issued());
		assertThat(entity.getPersonalNumber()).isEqualTo(SIGNING_INFO.user().personalNumber());
		assertThat(entity.getName()).isEqualTo(SIGNING_INFO.user().name());
		assertThat(entity.getGivenName()).isEqualTo(SIGNING_INFO.user().givenName());
		assertThat(entity.getSurname()).isEqualTo(SIGNING_INFO.user().surname());
		assertThat(entity.getUhi()).isEqualTo(SIGNING_INFO.device().uhi());
		assertThat(entity.getIpAddress()).isEqualTo(SIGNING_INFO.device().ipAddress());
		assertThat(entity.getMrtd()).isEqualTo(SIGNING_INFO.stepUp().mrtd());
		assertThat(entity.getRisk()).isEqualTo(SIGNING_INFO.risk());
	}

	@Test
	void testMapToMandateDetails() {
		final var entity = new MandateEntity()
			.withId(ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withName(NAME)
			.withGrantorPartyId(GRANTOR_PARTY_ID)
			.withSignatoryPartyId(SIGNATORY_PARTY_ID)
			.withGrantee(GRANTEE_PARTY_ID)
			.withActiveFrom(ACTIVE_FROM)
			.withInactiveAfter(INACTIVE_AFTER)
			.withCreated(OffsetDateTime.now().minusDays(2))
			.withUpdated(OffsetDateTime.now().minusDays(1))
			.withStatus(STATUS);

		final var details = mapper.toMandateDetails(entity);

		assertThat(details.id()).isEqualTo(entity.getId());
		assertThat(details.grantorDetails().name()).isEqualTo(NAME);
		assertThat(details.grantorDetails().grantorPartyId()).isEqualTo(GRANTOR_PARTY_ID);
		assertThat(details.grantorDetails().signatoryPartyId()).isEqualTo(SIGNATORY_PARTY_ID);
		assertThat(details.granteeDetails().partyId()).isEqualTo(GRANTEE_PARTY_ID);
		assertThat(details.municipalityId()).isEqualTo(MUNICIPALITY_ID);
		assertThat(details.namespace()).isEqualTo(NAMESPACE);
		assertThat(details.activeFrom()).isEqualTo(ACTIVE_FROM);
		assertThat(details.inactiveAfter()).isEqualTo(INACTIVE_AFTER);
		assertThat(details.created()).isEqualTo(entity.getCreated());
		assertThat(details.updated()).isEqualTo(entity.getUpdated());
		assertThat(details.status()).isEqualTo(STATUS);
	}

	@Test
	void testToSigningInfo() {
		final var signingInfo = mapper.toSigningInfo(SIGNING_INFORMATION_ENTITY);

		assertThat(signingInfo.status()).isEqualTo(SIGNING_INFORMATION_ENTITY.getStatus());
		assertThat(signingInfo.signed()).isEqualTo(SIGNING_INFORMATION_ENTITY.getSigned());
		assertThat(signingInfo.orderRef()).isEqualTo(SIGNING_INFORMATION_ENTITY.getOrderRef());
		assertThat(signingInfo.signature()).isEqualTo(SIGNING_INFORMATION_ENTITY.getSignatureData());
		assertThat(signingInfo.ocspResponse()).isEqualTo(SIGNING_INFORMATION_ENTITY.getOcspResponse());
		assertThat(signingInfo.issued()).isEqualTo(SIGNING_INFORMATION_ENTITY.getBankIdIssueDate());
		assertThat(signingInfo.user().personalNumber()).isEqualTo(SIGNING_INFORMATION_ENTITY.getPersonalNumber());
		assertThat(signingInfo.user().name()).isEqualTo(SIGNING_INFORMATION_ENTITY.getName());
		assertThat(signingInfo.user().givenName()).isEqualTo(SIGNING_INFORMATION_ENTITY.getGivenName());
		assertThat(signingInfo.user().surname()).isEqualTo(SIGNING_INFORMATION_ENTITY.getSurname());
		assertThat(signingInfo.device().uhi()).isEqualTo(SIGNING_INFORMATION_ENTITY.getUhi());
		assertThat(signingInfo.device().ipAddress()).isEqualTo(SIGNING_INFORMATION_ENTITY.getIpAddress());
		assertThat(signingInfo.stepUp().mrtd()).isEqualTo(SIGNING_INFORMATION_ENTITY.getMrtd());
		assertThat(signingInfo.risk()).isEqualTo(SIGNING_INFORMATION_ENTITY.getRisk());
	}

	@Test
	void testMapToMandateEntityWithNullBean() {
		assertThat(mapper.toMandateEntity(MUNICIPALITY_ID, NAMESPACE, null)).isNull();
		// Cannot test empty bean since everything is @NotNull
	}

	@Test
	void testMapToMandateDetailsWithNullAndEmptyBean() {
		assertThat(mapper.toMandateDetails(null)).isNull();
		assertThat(mapper.toMandateDetails(new MandateEntity())).isNotNull();
	}
}
