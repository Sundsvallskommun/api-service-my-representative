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

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import se.sundsvall.myrepresentative.api.model.CreateMandateBuilder;
import se.sundsvall.myrepresentative.api.model.GranteeDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.GrantorDetailsBuilder;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;

class MapperTest {

	private final Mapper mapper = new Mapper();

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

		// These shouldn't be set when mapping from API model to entity
		assertThat(entity.getId()).isNull();
		assertThat(entity.getCreated()).isNull();
		assertThat(entity.getUpdated()).isNull();
		assertThat(entity.getDeleted()).isEqualTo("false");
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
			.withCreated(LocalDateTime.now().minusDays(2))
			.withUpdated(LocalDateTime.now().minusDays(1))
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
}
