package se.sundsvall.myrepresentative.integration.db;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import org.springframework.stereotype.Component;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.GranteeDetails;
import se.sundsvall.myrepresentative.api.model.GrantorDetails;
import se.sundsvall.myrepresentative.api.model.SigningInfo;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;
import se.sundsvall.myrepresentative.integration.db.entity.SigningInformationEntity;

@Component
public class DatabaseMapper {

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
				.withName(of(mandate.grantorDetails())
					.map(GrantorDetails::name)
					.orElse(null))
				.withActiveFrom(mandate.activeFrom())
				.withInactiveAfter(mandate.inactiveAfter())
				.withNamespace(namespace)
				.addSigningInformation(toSigningInformationEntity(createMandate.signingInfo())))
			.orElse(null);
	}

	private SigningInformationEntity toSigningInformationEntity(final SigningInfo signingInfo) {
		return ofNullable(signingInfo)
			.map(info -> {
				var entity = new SigningInformationEntity()
					.withOrderRef(info.orderRef())
					.withStatus(info.status());

				of(info.completionData()).ifPresent(data -> {
					entity.withSignature(data.signature())
						.withOcspResponse(data.ocspResponse())
						.withBankIdIssueDate(data.bankIdIssueDate())
						.withRisk(data.risk());

					of(data.user()).ifPresent(user -> entity.withPersonalNumber(user.personalNumber())
						.withName(user.name())
						.withGivenName(user.givenName())
						.withSurname(user.surname()));

					of(data.device()).ifPresent(device -> entity.withIpAddress(device.ipAddress())
						.withUhi(device.uhi()));

					ofNullable(data.stepUp()).ifPresent(stepUp -> entity.withMrtdStepUp(stepUp.mrtd()));
				});

				return entity;
			})
			.orElse(null);
	}
}
