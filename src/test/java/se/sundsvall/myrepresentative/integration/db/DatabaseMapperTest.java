package se.sundsvall.myrepresentative.integration.db;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.myrepresentative.TestObjectFactory.ACTIVE_FROM;
import static se.sundsvall.myrepresentative.TestObjectFactory.GRANTEE_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.GRANTOR_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.INACTIVE_AFTER;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAME;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;
import static se.sundsvall.myrepresentative.TestObjectFactory.SIGNATORY_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.createMandate;

import org.junit.jupiter.api.Test;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.integration.db.entity.SigningInformationEntity;

class DatabaseMapperTest {

	private final DatabaseMapper mapper = new DatabaseMapper();

	@Test
	void testMapToMandateEntity() {
		final var createMandate = createMandate();

		final var entity = mapper.toMandateEntity(MUNICIPALITY_ID, NAMESPACE, createMandate);

		assertThat(entity.getName()).isEqualTo(NAME);
		assertThat(entity.getGrantorPartyId()).isEqualTo(GRANTOR_PARTY_ID);
		assertThat(entity.getSignatoryPartyId()).isEqualTo(SIGNATORY_PARTY_ID);
		assertThat(entity.getGranteePartyId()).isEqualTo(GRANTEE_PARTY_ID);
		assertThat(entity.getNamespace()).isEqualTo(NAMESPACE);
		assertThat(entity.getMunicipalityId()).isEqualTo(MUNICIPALITY_ID);
		assertThat(entity.getActiveFrom()).isEqualTo(ACTIVE_FROM);
		assertThat(entity.getInactiveAfter()).isEqualTo(INACTIVE_AFTER);
		assertThat(entity.getCreated()).isNull();
		assertSigningInformation(entity.getLatestSigningInformation(), createMandate);

		// These shouldn't be set when mapping from API model to entity
		assertThat(entity.getId()).isNull();
		assertThat(entity.getCreated()).isNull();
		assertThat(entity.getUpdated()).isNull();
		assertThat(entity.getDeleted()).isNull();
	}

	@Test
	void testMapToMandateEntityWithNullBean() {
		assertThat(mapper.toMandateEntity(MUNICIPALITY_ID, NAMESPACE, null)).isNull();
	}

	private void assertSigningInformation(SigningInformationEntity entity, CreateMandate createMandate) {
		assertThat(entity.getStatus()).isEqualTo(createMandate.signingInfo().status());
		assertThat(entity.getOrderRef()).isEqualTo(createMandate.signingInfo().orderRef());
		assertThat(entity.getSignature()).isEqualTo(createMandate.signingInfo().completionData().signature());
		assertThat(entity.getOcspResponse()).isEqualTo(createMandate.signingInfo().completionData().ocspResponse());
		assertThat(entity.getBankIdIssueDate()).isEqualTo(createMandate.signingInfo().completionData().bankIdIssueDate());
		assertThat(entity.getPersonalNumber()).isEqualTo(createMandate.signingInfo().completionData().user().personalNumber());
		assertThat(entity.getName()).isEqualTo(createMandate.signingInfo().completionData().user().name());
		assertThat(entity.getGivenName()).isEqualTo(createMandate.signingInfo().completionData().user().givenName());
		assertThat(entity.getSurname()).isEqualTo(createMandate.signingInfo().completionData().user().surname());
		assertThat(entity.getUhi()).isEqualTo(createMandate.signingInfo().completionData().device().uhi());
		assertThat(entity.getIpAddress()).isEqualTo(createMandate.signingInfo().completionData().device().ipAddress());
		assertThat(entity.getMrtd()).isEqualTo(createMandate.signingInfo().completionData().stepUp().mrtd());
		assertThat(entity.getRisk()).isEqualTo(createMandate.signingInfo().completionData().risk());
	}

}
