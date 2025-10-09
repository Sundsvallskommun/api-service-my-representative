package se.sundsvall.myrepresentative.integration.db;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import org.springframework.stereotype.Component;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.GranteeDetails;
import se.sundsvall.myrepresentative.api.model.GrantorDetails;
import se.sundsvall.myrepresentative.api.model.SigningInfo;
import se.sundsvall.myrepresentative.api.model.SigningInfo.Device;
import se.sundsvall.myrepresentative.api.model.SigningInfo.StepUp;
import se.sundsvall.myrepresentative.api.model.SigningInfo.User;
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
			.map(info -> new SigningInformationEntity()
				.withStatus(signingInfo.status())
				.withSigned(signingInfo.signed())
				.withOrderRef(signingInfo.orderRef())
				.withSignatureData(signingInfo.signature())
				.withOcspResponse(signingInfo.ocspResponse())
				.withBankIdIssueDate(signingInfo.issued())
				.withPersonalNumber(of(signingInfo.user())
					.map(User::personalNumber)
					.orElse(null))
				.withName(of(signingInfo.user())
					.map(User::name)
					.orElse(null))
				.withGivenName(of(signingInfo.user())
					.map(User::givenName)
					.orElse(null))
				.withSurname(of(signingInfo.user())
					.map(User::surname)
					.orElse(null))
				.withUhi(of(signingInfo.device())
					.map(Device::uhi)
					.orElse(null))
				.withIpAddress(of(signingInfo.device())
					.map(Device::ipAddress)
					.orElse(null))
				.withMrtdStepUp(of(signingInfo.stepUp())
					.map(StepUp::mrtd)
					.orElse(null))
				.withRisk(signingInfo.risk()))
			.orElse(null);
	}
}
