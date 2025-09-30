package se.sundsvall.myrepresentative.service;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import org.springframework.stereotype.Component;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.GranteeDetails;
import se.sundsvall.myrepresentative.api.model.GranteeDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.GrantorDetails;
import se.sundsvall.myrepresentative.api.model.GrantorDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.MandateDetails;
import se.sundsvall.myrepresentative.api.model.MandateDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.MandateStatus;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;

@Component
public class Mapper {

	public MandateEntity toMandateEntity(final String municipalityId, final String namespace, final CreateMandate createMandate) {
		return ofNullable(createMandate)
			.map(mandate -> new MandateEntity()
				.withMunicipalityId(municipalityId)
				.withGrantorPartyId(of(mandate.grantorDetails())
					.map(GrantorDetails::grantorPartyId)
					.orElse(null))
				.withSignatoryPartyId(of(mandate.grantorDetails())
					.map(GrantorDetails::signatoryPartyId)
					.orElse(null))
				.withGrantee(of(mandate.granteeDetails())
					.map(GranteeDetails::partyId)
					.orElse(null))
				.withName(mandate.grantorDetails().name())
				.withActiveFrom(mandate.activeFrom())
				.withInactiveAfter(mandate.inactiveAfter())
				.withNamespace(namespace))
			.orElse(null);
	}

	public MandateDetails toMandateDetails(final MandateEntity mandateEntity) {
		return ofNullable(mandateEntity)
			.map(entity -> MandateDetailsBuilder.create()
				.withId(entity.getId())
				.withGrantorDetails(toGrantorDetails(entity))
				.withGranteeDetails(toGranteeDetails(entity))
				.withMunicipalityId(entity.getMunicipalityId())
				.withNamespace(entity.getNamespace())
				.withCreated(entity.getCreated())
				.withUpdated(entity.getUpdated())
				.withActiveFrom(entity.getActiveFrom())
				.withInactiveAfter(entity.getInactiveAfter())
				.withStatus(ofNullable(entity.getStatus())
					.map(status -> MandateStatus.valueOf(status).toString())
					.orElse(MandateStatus.UNKNOWN.toString()))
				.build())
			.orElse(null);
	}

	private GrantorDetails toGrantorDetails(final MandateEntity mandateEntity) {
		return ofNullable(mandateEntity)
			.map(entity -> GrantorDetailsBuilder.create()
				.withGrantorPartyId(entity.getGrantorPartyId())
				.withSignatoryPartyId(entity.getSignatoryPartyId())
				.withName(entity.getName())
				.build())
			.orElse(null);
	}

	private GranteeDetails toGranteeDetails(final MandateEntity mandateEntity) {
		return ofNullable(mandateEntity)
			.map(entity -> GranteeDetailsBuilder.create()
				.withPartyId(mandateEntity.getGranteePartyId())
				.build())
			.orElse(null);
	}
}
