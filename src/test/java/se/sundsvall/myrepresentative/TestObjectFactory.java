package se.sundsvall.myrepresentative;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.CreateMandateBuilder;
import se.sundsvall.myrepresentative.api.model.GranteeDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.GrantorDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.UpdateMandate;
import se.sundsvall.myrepresentative.api.model.UpdateMandateBuilder;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;

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
	public static final LocalDate INACTIVE_AFTER = LocalDate.now().plusDays(1);
	public static final LocalDateTime CREATED = LocalDateTime.now().minusMinutes(1);
	public static final LocalDateTime UPDATED = LocalDateTime.now();
	public static final String DELETED = "false";
	public static final String STATUS = "ACTIVE";

	public static CreateMandate createMandate() {
		return CreateMandateBuilder.create()
			.withGrantorDetails(GrantorDetailsBuilder.create()
				.withSignatoryPartyId(UUID.randomUUID().toString())
				.withGrantorPartyId(UUID.randomUUID().toString())
				.withSignatoryPartyId(UUID.randomUUID().toString())
				.build())
			.withGranteeDetails(GranteeDetailsBuilder.create()
				.withPartyId(UUID.randomUUID().toString())
				.build())
			.withActiveFrom(LocalDate.now())
			.withInactiveAfter(LocalDate.now().plusDays(10))
			.build();
	}

	public static UpdateMandate updateMandate() {
		return UpdateMandateBuilder.create()
			.withGranteeDetails(GranteeDetailsBuilder.create()
				.withPartyId(UUID.randomUUID().toString())
				.build())
			.withActiveFrom(LocalDate.now())
			.withInactiveAfter(LocalDate.now().plusDays(10))
			.build();
	}

	public static MandateEntity createMandateEntity(boolean isDeleted) {
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
			.withDeleted(isDeleted ? LocalDateTime.now().toString() : DELETED)
			.withStatus(STATUS);
	}
}
