package se.sundsvall.myrepresentative.service;

import static org.assertj.core.api.Assertions.assertThat;
import static se.sundsvall.myrepresentative.TestObjectFactory.ACTIVE_FROM;
import static se.sundsvall.myrepresentative.TestObjectFactory.GRANTEE_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.GRANTOR_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.INACTIVE_AFTER;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAME;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;
import static se.sundsvall.myrepresentative.TestObjectFactory.SIGNATORY_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.STATUS;
import static se.sundsvall.myrepresentative.TestObjectFactory.createMandateEntity;
import static se.sundsvall.myrepresentative.TestObjectFactory.createSigningInfoEntity;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;
import se.sundsvall.myrepresentative.integration.db.entity.SigningInformationEntity;

class ServiceMapperTest {

	private final ServiceMapper serviceMapper = new ServiceMapper();
	private static final SigningInformationEntity SIGNING_INFORMATION_ENTITY = createSigningInfoEntity();

	@Test
	void testMapToMandateDetails() {
		final var entity = createMandateEntity(false);

		final var details = serviceMapper.toMandateDetailsWithSigningInfo(entity);

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
		final var signingInfo = serviceMapper.toSigningInfo(SIGNING_INFORMATION_ENTITY);

		assertThat(signingInfo.status()).isEqualTo(SIGNING_INFORMATION_ENTITY.getStatus());
		assertThat(signingInfo.orderRef()).isEqualTo(SIGNING_INFORMATION_ENTITY.getOrderRef());
		assertThat(signingInfo.externalTransactionId()).isEqualTo(SIGNING_INFORMATION_ENTITY.getExternalTransactionId());
		assertThat(signingInfo.completionData().signature()).isEqualTo(SIGNING_INFORMATION_ENTITY.getSignature());
		assertThat(signingInfo.completionData().ocspResponse()).isEqualTo(SIGNING_INFORMATION_ENTITY.getOcspResponse());
		assertThat(signingInfo.completionData().bankIdIssueDate()).isEqualTo(SIGNING_INFORMATION_ENTITY.getBankIdIssueDate());
		assertThat(signingInfo.completionData().user().personalNumber()).isEqualTo(SIGNING_INFORMATION_ENTITY.getPersonalNumber());
		assertThat(signingInfo.completionData().user().name()).isEqualTo(SIGNING_INFORMATION_ENTITY.getName());
		assertThat(signingInfo.completionData().user().givenName()).isEqualTo(SIGNING_INFORMATION_ENTITY.getGivenName());
		assertThat(signingInfo.completionData().user().surname()).isEqualTo(SIGNING_INFORMATION_ENTITY.getSurname());
		assertThat(signingInfo.completionData().device().uhi()).isEqualTo(SIGNING_INFORMATION_ENTITY.getUhi());
		assertThat(signingInfo.completionData().device().ipAddress()).isEqualTo(SIGNING_INFORMATION_ENTITY.getIpAddress());
		assertThat(signingInfo.completionData().stepUp().mrtd()).isEqualTo(SIGNING_INFORMATION_ENTITY.getMrtd());
		assertThat(signingInfo.completionData().risk()).isEqualTo(SIGNING_INFORMATION_ENTITY.getRisk());
	}

	@Test
	void testToMandates() {
		final var entity = createMandateEntity(true);
		final var entity2 = createMandateEntity(false);
		final Page<MandateEntity> page = new PageImpl<>(List.of(entity, entity2));
		final var mandates = serviceMapper.toMandates(page);

		assertThat(mandates.mandateDetailsList()).hasSize(2);

		assertThat(mandates.metaData()).isNotNull();
		assertThat(mandates.metaData().getPage()).isEqualTo(1);
		assertThat(mandates.metaData().getLimit()).isEqualTo(2);
		assertThat(mandates.metaData().getCount()).isEqualTo(2);
		assertThat(mandates.metaData().getTotalRecords()).isEqualTo(2);
		assertThat(mandates.metaData().getTotalPages()).isEqualTo(1);
		assertThat(mandates.metaData().getSortBy()).isNull();
		assertThat(mandates.metaData().getSortDirection()).isNull();
	}

	@Test
	void testToSigningInfoWithNullBean() {
		assertThat(serviceMapper.toSigningInfo(null)).isNull();
	}

	@Test
	void testToMandatesWithNullBean() {
		assertThat(serviceMapper.toMandates(null)).isNull();
	}

	@Test
	void testToMandateDetailsWithSigningInfoWithNullBean() {
		assertThat(serviceMapper.toMandateDetailsWithSigningInfo(null)).isNull();
	}

	@Test
	void testToMandateDetailWithoutSigningInfoWithNullBean() {
		assertThat(serviceMapper.toMandateDetailsWithSigningInfo(null)).isNull();
	}

	@Test
	void testMapToMandateDetailsWithNullAndEmptyBean() {
		assertThat(serviceMapper.toMandateDetailsWithSigningInfo(null)).isNull();
		assertThat(serviceMapper.toMandateDetailsWithSigningInfo(new MandateEntity())).isNotNull();
	}
}
