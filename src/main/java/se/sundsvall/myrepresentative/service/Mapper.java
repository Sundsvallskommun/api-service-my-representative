package se.sundsvall.myrepresentative.service;

import org.springframework.stereotype.Component;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.GranteeDetails;
import se.sundsvall.myrepresentative.api.model.GrantorDetails;
import se.sundsvall.myrepresentative.api.model.MandateDetails;
import se.sundsvall.myrepresentative.api.model.MandateStatus;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;

@Component
public class Mapper {

	public MandateEntity toMandateEntity(final String municipalityId, final String namespace, final CreateMandate mandate) {
		return new MandateEntity()
			.withMunicipalityId(municipalityId)
			.withGrantorPartyId(mandate.grantorDetails().grantorPartyId())
			.withSignatoryPartyId(mandate.grantorDetails().signatoryPartyId())
			.withGrantee(mandate.granteeDetails().partyId())
			.withName(mandate.grantorDetails().name())
			.withActiveFrom(mandate.activeFrom())
			.withInactiveAfter(mandate.inactiveAfter())
			.withNamespace(namespace);
	}

	public MandateDetails toMandateDetails(final MandateEntity mandateEntity) {
		return new MandateDetails(
			mandateEntity.getId(),
			toGrantorDetails(mandateEntity),
			toGranteeDetails(mandateEntity),
			mandateEntity.getMunicipalityId(),
			mandateEntity.getNamespace(),
			mandateEntity.getCreated(),
			mandateEntity.getUpdated(),
			mandateEntity.getActiveFrom(),
			mandateEntity.getInactiveAfter(),
			MandateStatus.valueOf(mandateEntity.getStatus()).toString());
	}

	private GrantorDetails toGrantorDetails(final MandateEntity mandateEntity) {
		return new GrantorDetails(mandateEntity.getName(), mandateEntity.getGrantorPartyId(), mandateEntity.getSignatoryPartyId());
	}

	private GranteeDetails toGranteeDetails(final MandateEntity mandateEntity) {
		return new GranteeDetails(mandateEntity.getGranteePartyId());
	}
}
