package se.sundsvall.myrepresentative.service;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;

import org.springframework.stereotype.Component;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.DeviceBuilder;
import se.sundsvall.myrepresentative.api.model.GranteeDetails;
import se.sundsvall.myrepresentative.api.model.GranteeDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.GrantorDetails;
import se.sundsvall.myrepresentative.api.model.GrantorDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.MandateDetails;
import se.sundsvall.myrepresentative.api.model.MandateDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.MandateStatus;
import se.sundsvall.myrepresentative.api.model.SigningInfo;
import se.sundsvall.myrepresentative.api.model.SigningInfoBuilder;
import se.sundsvall.myrepresentative.api.model.StepUpBuilder;
import se.sundsvall.myrepresentative.api.model.UserBuilder;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;
import se.sundsvall.myrepresentative.integration.db.entity.SigningInformationEntity;

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
					.map(SigningInfo.User::personalNumber)
					.orElse(null))
				.withName(of(signingInfo.user())
					.map(SigningInfo.User::name)
					.orElse(null))
				.withGivenName(of(signingInfo.user())
					.map(SigningInfo.User::givenName)
					.orElse(null))
				.withSurname(of(signingInfo.user())
					.map(SigningInfo.User::surname)
					.orElse(null))
				.withUhi(of(signingInfo.device())
					.map(SigningInfo.Device::uhi)
					.orElse(null))
				.withIpAddress(of(signingInfo.device())
					.map(SigningInfo.Device::ipAddress)
					.orElse(null))
				.withMrtdStepUp(of(signingInfo.stepUp())
					.map(SigningInfo.StepUp::mrtd)
					.orElse(null))
				.withRisk(signingInfo.risk()))
			.orElse(null);
	}

	public SigningInfo toSigningInfo(final SigningInformationEntity entity) {
		return ofNullable(entity)
			.map(info -> SigningInfoBuilder.create()
				.withStatus(entity.getStatus())
				.withSigned(entity.getSigned())
				.withOrderRef(entity.getOrderRef())
				.withSignature(entity.getSignatureData())
				.withOcspResponse(entity.getOcspResponse())
				.withIssued(entity.getBankIdIssueDate())
				.withUser(toSigningInfoUser(entity))
				.withDevice(toSigningInfoDevice(entity))
				.withStepUp(toSigningInfoStepUp(entity))
				.withRisk(entity.getRisk())
				.build())
			.orElse(null);
	}

	private SigningInfo.User toSigningInfoUser(final SigningInformationEntity signingInformation) {
		return ofNullable(signingInformation)
			.map(info -> UserBuilder.create()
				.withPersonalNumber(signingInformation.getPersonalNumber())
				.withName(signingInformation.getName())
				.withGivenName(signingInformation.getGivenName())
				.withSurname(signingInformation.getSurname())
				.build())
			.orElse(null);
	}

	private SigningInfo.StepUp toSigningInfoStepUp(final SigningInformationEntity signingInformation) {
		return ofNullable(signingInformation)
			.map(info -> StepUpBuilder.create()
				.withMrtd(signingInformation.getMrtd())
				.build())
			.orElse(null);
	}

	private SigningInfo.Device toSigningInfoDevice(final SigningInformationEntity signingInformation) {
		return ofNullable(signingInformation)
			.map(info -> DeviceBuilder.create()
				.withUhi(signingInformation.getUhi())
				.withIpAddress(signingInformation.getIpAddress())
				.build())
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
				.withSigningInfo(toSigningInfo(entity.getLatestSigningInformation()))
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
