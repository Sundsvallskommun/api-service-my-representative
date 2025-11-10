package se.sundsvall.myrepresentative;

import generated.se.sundsvall.legalentity.PersonEngagement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import se.sundsvall.myrepresentative.api.model.CompletionDataBuilder;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.CreateMandateBuilder;
import se.sundsvall.myrepresentative.api.model.DeviceBuilder;
import se.sundsvall.myrepresentative.api.model.GranteeDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.GrantorDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.SigningInfo;
import se.sundsvall.myrepresentative.api.model.SigningInfoBuilder;
import se.sundsvall.myrepresentative.api.model.StepUpBuilder;
import se.sundsvall.myrepresentative.api.model.UpdateMandate;
import se.sundsvall.myrepresentative.api.model.UpdateMandateBuilder;
import se.sundsvall.myrepresentative.api.model.UserBuilder;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;
import se.sundsvall.myrepresentative.integration.db.entity.SigningInformationEntity;

public final class TestObjectFactory {

	private TestObjectFactory() {}

	public static final String ID = UUID.randomUUID().toString();
	public static final String MUNICIPALITY_ID = "2281";
	public static final String NAMESPACE = "my-namespace";
	public static final String NAME = "Ankeborgs Margarinfabrik";
	public static final String GRANTOR_PARTY_ID = UUID.randomUUID().toString();
	public static final String SIGNATORY_PARTY_ID = UUID.randomUUID().toString();
	public static final String GRANTEE_PARTY_ID = UUID.randomUUID().toString();
	public static final LocalDate ACTIVE_FROM = LocalDate.now().minusDays(1);
	public static final LocalDate INACTIVE_AFTER = LocalDate.now().plusDays(10);
	public static final OffsetDateTime CREATED = OffsetDateTime.now().minusMinutes(1);
	public static final OffsetDateTime UPDATED = OffsetDateTime.now();
	// Represents a non-deleted-mandate at time 1970-01-01T00:00Z
	public static final OffsetDateTime NOT_DELETED = OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
	public static final String STATUS = "ACTIVE";

	public static CreateMandate createMandate() {
		return CreateMandateBuilder.create()
			.withGrantorDetails(GrantorDetailsBuilder.create()
				.withSignatoryPartyId(SIGNATORY_PARTY_ID)
				.withGrantorPartyId(GRANTOR_PARTY_ID)
				.withName(NAME)
				.build())
			.withGranteeDetails(GranteeDetailsBuilder.create()
				.withPartyId(GRANTEE_PARTY_ID)
				.build())
			.withActiveFrom(ACTIVE_FROM)
			.withInactiveAfter(INACTIVE_AFTER)
			.withSigningInfo(createSigningInfo())
			.build();
	}

	public static UpdateMandate updateMandate() {
		return UpdateMandateBuilder.create()
			.withGranteeDetails(GranteeDetailsBuilder.create()
				.withPartyId(GRANTEE_PARTY_ID)
				.build())
			.withActiveFrom(ACTIVE_FROM)
			.withInactiveAfter(INACTIVE_AFTER)
			.build();
	}

	public static MandateEntity createMandateEntity(final boolean isDeleted) {
		return new MandateEntity()
			.withId(ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withNamespace(NAMESPACE)
			.withName(NAME)
			.withGrantorPartyId(GRANTOR_PARTY_ID)
			.withSignatoryPartyId(SIGNATORY_PARTY_ID)
			.withGrantee(GRANTEE_PARTY_ID)
			.withActiveFrom(ACTIVE_FROM)
			.withInactiveAfter(INACTIVE_AFTER)
			.withCreated(CREATED)
			.withUpdated(UPDATED)
			// If true, set deleted to current time, else set to "false"
			.withDeleted(isDeleted ? OffsetDateTime.now() : NOT_DELETED)
			.withStatus(STATUS);
	}

	public static SigningInfo createSigningInfo() {
		return SigningInfoBuilder.create()
			.withStatus("complete")
			.withOrderRef(UUID.randomUUID().toString())
			.withExternalTransactionId(UUID.randomUUID().toString())
			.withCompletionData(CompletionDataBuilder.create()
				.withSignature("YmFzZTY0LWVuY29kZWQgZGF0YQ==")
				.withOcspResponse("YmFzZTY0LWVuY29kZWQgZGF0YQ==")
				.withBankIdIssueDate(LocalDate.now().minusYears(1))
				.withUser(UserBuilder.create()
					.withPersonalNumber("200001012384")
					.withName("John Wick")
					.withGivenName("John")
					.withSurname("Wick")
					.build())
				.withDevice(DeviceBuilder.create()
					.withUhi("OZvYM9VvyiAmG7NA5jU5zqGcVpo=")
					.withIpAddress("192.168.1.1")
					.build())
				.withStepUp(StepUpBuilder.create()
					.withMrtd(true)
					.build())
				.withRisk("low")
				.build())
			.build();
	}

	public static SigningInformationEntity createSigningInfoEntity() {
		return new SigningInformationEntity()
			.withStatus("complete")
			.withCreated(OffsetDateTime.now().minusDays(1))
			.withOrderRef(UUID.randomUUID().toString())
			.withExternalTransactionId(UUID.randomUUID().toString())
			.withSignature("YmFzZTY0LWVuY29kZWQgZGF0YQ==")
			.withOcspResponse("YmFzZTY0LWVuY29kZWQgZGF0YQ==")
			.withBankIdIssueDate(LocalDate.now().minusYears(1))
			.withPersonalNumber("200001012384")
			.withName("John Wick")
			.withGivenName("John")
			.withSurname("Wick")
			.withIpAddress("192.168.1.")
			.withUhi("OZvYM9VvyiAmG7NA5jU5zqGcVpo=")
			.withMrtdStepUp(true)
			.withMandate(createMandateEntity(false))
			.withRisk("low");
	}

	public static PersonEngagement createPersonEngagement() {
		final var engagement = new PersonEngagement();
		engagement.setOrganizationNumber("1234567890");
		engagement.setIsAuthorizedSignatory(true);
		return engagement;
	}
}
