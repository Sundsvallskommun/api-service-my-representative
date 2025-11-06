package se.sundsvall.myrepresentative.service;

import static java.util.Optional.ofNullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;
import se.sundsvall.myrepresentative.api.model.CompletionDataBuilder;
import se.sundsvall.myrepresentative.api.model.DeviceBuilder;
import se.sundsvall.myrepresentative.api.model.GranteeDetails;
import se.sundsvall.myrepresentative.api.model.GranteeDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.GrantorDetails;
import se.sundsvall.myrepresentative.api.model.GrantorDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.MandateDetails;
import se.sundsvall.myrepresentative.api.model.MandateDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.MandateStatus;
import se.sundsvall.myrepresentative.api.model.Mandates;
import se.sundsvall.myrepresentative.api.model.MandatesBuilder;
import se.sundsvall.myrepresentative.api.model.SigningInfo;
import se.sundsvall.myrepresentative.api.model.SigningInfo.CompletionData;
import se.sundsvall.myrepresentative.api.model.SigningInfo.CompletionData.Device;
import se.sundsvall.myrepresentative.api.model.SigningInfo.CompletionData.StepUp;
import se.sundsvall.myrepresentative.api.model.SigningInfo.CompletionData.User;
import se.sundsvall.myrepresentative.api.model.SigningInfoBuilder;
import se.sundsvall.myrepresentative.api.model.StepUpBuilder;
import se.sundsvall.myrepresentative.api.model.UserBuilder;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;
import se.sundsvall.myrepresentative.integration.db.entity.SigningInformationEntity;

@Component
public class ServiceMapper {

	public SigningInfo toSigningInfo(final SigningInformationEntity entity) {
		return ofNullable(entity)
			.map(info -> SigningInfoBuilder.create()
				.withStatus(entity.getStatus())
				.withOrderRef(entity.getOrderRef())
				.withExternalTransactionId(entity.getExternalTransactionId())
				.withCompletionData(toCompletionData(entity))
				.build())
			.orElse(null);
	}

	private CompletionData toCompletionData(final SigningInformationEntity entity) {
		return ofNullable(entity)
			.map(info -> CompletionDataBuilder.create()
				.withBankIdIssueDate(entity.getBankIdIssueDate())
				.withSignature(entity.getSignature())
				.withOcspResponse(entity.getOcspResponse())
				.withRisk(entity.getRisk())
				.withUser(toSigningInfoUser(entity))
				.withDevice(toSigningInfoDevice(entity))
				.withStepUp(toSigningInfoStepUp(entity))
				.build())
			.orElse(null);
	}

	private User toSigningInfoUser(final SigningInformationEntity signingInformation) {
		return ofNullable(signingInformation)
			.map(info -> UserBuilder.create()
				.withPersonalNumber(signingInformation.getPersonalNumber())
				.withName(signingInformation.getName())
				.withGivenName(signingInformation.getGivenName())
				.withSurname(signingInformation.getSurname())
				.build())
			.orElse(null);
	}

	private StepUp toSigningInfoStepUp(final SigningInformationEntity signingInformation) {
		return ofNullable(signingInformation)
			.map(info -> StepUpBuilder.create()
				.withMrtd(signingInformation.getMrtd())
				.build())
			.orElse(null);
	}

	private Device toSigningInfoDevice(final SigningInformationEntity signingInformation) {
		return ofNullable(signingInformation)
			.map(info -> DeviceBuilder.create()
				.withUhi(signingInformation.getUhi())
				.withIpAddress(signingInformation.getIpAddress())
				.build())
			.orElse(null);
	}

	public Mandates toMandates(final Page<MandateEntity> mandatePage) {
		return ofNullable(mandatePage)
			.map(page -> MandatesBuilder.create()
				.withMandateDetailsList(toMandateDetailsList(page.getContent()))
				.withMetaData(PagingAndSortingMetaData.create().withPageData(page))
				.build())
			.orElse(null);
	}

	private List<MandateDetails> toMandateDetailsList(final List<MandateEntity> entities) {
		return ofNullable(entities)
			.orElse(Collections.emptyList())
			.stream()
			.map(this::toMandateDetailsWithoutSigningInfo)
			.filter(Objects::nonNull)
			.toList();
	}

	public MandateDetails toMandateDetailsWithSigningInfo(final MandateEntity mandateEntity) {
		return toMandateDetails(mandateEntity, true);
	}

	public MandateDetails toMandateDetailsWithoutSigningInfo(final MandateEntity mandateEntity) {
		return toMandateDetails(mandateEntity, false);
	}

	private MandateDetails toMandateDetails(final MandateEntity mandateEntity, final boolean includeSigningInfo) {
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
				// Include signing info conditionally
				.withSigningInfo(includeSigningInfo ? toSigningInfo(entity.getLatestSigningInformation()) : null)
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
