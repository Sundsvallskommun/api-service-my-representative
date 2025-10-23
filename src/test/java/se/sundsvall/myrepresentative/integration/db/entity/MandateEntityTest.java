package se.sundsvall.myrepresentative.integration.db.entity;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsFor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCodeFor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToStringExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSettersExcluding;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.OffsetDateTime.now;
import static java.time.ZoneId.systemDefault;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.within;
import static org.hamcrest.CoreMatchers.allOf;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;
import static se.sundsvall.myrepresentative.TestObjectFactory.ACTIVE_FROM;
import static se.sundsvall.myrepresentative.TestObjectFactory.CREATED;
import static se.sundsvall.myrepresentative.TestObjectFactory.GRANTEE_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.GRANTOR_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.INACTIVE_AFTER;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAME;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;
import static se.sundsvall.myrepresentative.TestObjectFactory.NOT_DELETED;
import static se.sundsvall.myrepresentative.TestObjectFactory.SIGNATORY_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.STATUS;
import static se.sundsvall.myrepresentative.TestObjectFactory.UPDATED;
import static se.sundsvall.myrepresentative.TestObjectFactory.createSigningInfoEntity;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.zalando.problem.ThrowableProblem;

class MandateEntityTest {

	private static final SigningInformationEntity SIGNING_INFORMATION = createSigningInfoEntity();

	@BeforeAll
	static void beforeAll() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), OffsetDateTime.class);
		registerValueGenerator(() -> LocalDate.now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(MandateEntity.class, allOf(
			hasValidGettersAndSettersExcluding("latestSigningInformation", "signingInformation"),
			hasValidBeanConstructor(),
			hasValidBeanHashCodeFor("id"),
			hasValidBeanEqualsFor("id"),
			hasValidBeanToStringExcluding("ocspResponse", "signatureData", "latestSigningInformation", "signingInformation")));
	}

	@Test
	void testBuilders() {
		final var entity = new MandateEntity()
			.withId(ID)
			.withName(NAME)
			.withGrantorPartyId(GRANTOR_PARTY_ID)
			.withSignatoryPartyId(SIGNATORY_PARTY_ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withCreated(CREATED)
			.withUpdated(UPDATED)
			.withActiveFrom(ACTIVE_FROM)
			.withInactiveAfter(INACTIVE_AFTER)
			.withDeleted(NOT_DELETED)
			.withGrantee(GRANTEE_PARTY_ID)
			.withNamespace(NAMESPACE)
			.withStatus(STATUS)
			.addSigningInformation(SIGNING_INFORMATION);

		assertBean(entity);
	}

	@Test
	void testSetterAndGetters() {
		final var entity = new MandateEntity();
		entity.setId(ID);
		entity.setName(NAME);
		entity.setGrantorPartyId(GRANTOR_PARTY_ID);
		entity.setSignatoryPartyId(SIGNATORY_PARTY_ID);
		entity.setMunicipalityId(MUNICIPALITY_ID);
		entity.setCreated(CREATED);
		entity.setUpdated(UPDATED);
		entity.setActiveFrom(ACTIVE_FROM);
		entity.setInactiveAfter(INACTIVE_AFTER);
		entity.setDeleted(NOT_DELETED);
		entity.setGranteePartyId(GRANTEE_PARTY_ID);
		entity.setNamespace(NAMESPACE);
		entity.setStatus(STATUS);
		entity.addSigningInformation(SIGNING_INFORMATION); // Not really a setter but it's needed for the assertBean

		assertBean(entity);
	}

	private void assertBean(final MandateEntity entity) {
		assertThat(entity.getId()).isEqualTo(ID);
		assertThat(entity.getName()).isEqualTo(NAME);
		assertThat(entity.getGrantorPartyId()).isEqualTo(GRANTOR_PARTY_ID);
		assertThat(entity.getSignatoryPartyId()).isEqualTo(SIGNATORY_PARTY_ID);
		assertThat(entity.getMunicipalityId()).isEqualTo(MUNICIPALITY_ID);
		assertThat(entity.getCreated()).isEqualTo(CREATED);
		assertThat(entity.getUpdated()).isEqualTo(UPDATED);
		assertThat(entity.getActiveFrom()).isEqualTo(ACTIVE_FROM);
		assertThat(entity.getInactiveAfter()).isEqualTo(INACTIVE_AFTER);
		assertThat(entity.getDeleted()).isEqualTo(NOT_DELETED);
		assertThat(entity.getGranteePartyId()).isEqualTo(GRANTEE_PARTY_ID);
		assertThat(entity.getStatus()).isEqualTo(STATUS);
		assertThat(entity.getSigningInformation()).containsExactly(SIGNING_INFORMATION);

		assertThat(entity).hasNoNullFieldsOrProperties();
	}

	@Test
	void testAddAndGetLatestSigningInformation() {
		var latestSigningInformation = createSigningInfoEntity();
		latestSigningInformation.setCreated(OffsetDateTime.now(systemDefault()).truncatedTo(ChronoUnit.MILLIS));
		var entity = new MandateEntity()
			.addSigningInformation(SIGNING_INFORMATION)
			.addSigningInformation(latestSigningInformation);

		assertThat(entity.getLatestSigningInformation()).isEqualTo(latestSigningInformation);
	}

	@Test
	void testAddAndGetAllSigningInformation() {
		var latestSigningInformation = createSigningInfoEntity();
		var entity = new MandateEntity()
			.addSigningInformation(SIGNING_INFORMATION)
			.addSigningInformation(latestSigningInformation);

		assertThat(entity.getSigningInformation()).containsExactly(SIGNING_INFORMATION, latestSigningInformation);
	}

	@Test
	void testGetSigningInformationReturnsUnmodifiableList() {
		var entity = new MandateEntity()
			.addSigningInformation(SIGNING_INFORMATION);

		assertThatExceptionOfType(UnsupportedOperationException.class)
			.isThrownBy(() -> entity.getSigningInformation().add(createSigningInfoEntity()));
	}

	@Test
	void testPrePersist() {
		final var entity = new MandateEntity();
		assertThat(entity.getCreated()).isNull();
		assertThat(entity.getUpdated()).isNull();

		entity.withActiveFrom(ACTIVE_FROM); // We need activeFrom to be able to calculate inactiveAfter
		entity.onCreate();

		assertThat(entity.getCreated()).isNotNull().isCloseTo(now(systemDefault()), within(2, ChronoUnit.SECONDS));
		assertThat(entity.getUpdated()).isNotNull().isCloseTo(now(systemDefault()), within(2, ChronoUnit.SECONDS));
		assertThat(entity.getActiveFrom()).isEqualTo(ACTIVE_FROM);
		assertThat(entity.getInactiveAfter()).isEqualTo(ACTIVE_FROM.plusMonths(36));
	}

	@Test
	void testPrePersistWithInvalidDates_shouldThrowProblem() {
		final var entity = new MandateEntity();

		assertThatExceptionOfType(ThrowableProblem.class).isThrownBy(entity::onCreate)
			.satisfies(problem -> {
				assertThat(problem.getStatus()).isEqualTo(INTERNAL_SERVER_ERROR);
				assertThat(problem.getTitle()).isEqualTo("Cannot set validity period for mandate");
				assertThat(problem.getDetail()).isEqualTo("activeFrom and/or incactiveAfter is null, cannot set validity period");
			});
	}

	@Test
	void testPreUpdate() {
		final var preUpdate = OffsetDateTime.now().minusSeconds(1);
		final var entity = new MandateEntity();
		entity.setUpdated(preUpdate);

		entity.onUpdate();

		assertThat(entity.getUpdated()).isNotNull().isAfter(preUpdate);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new MandateEntity()).hasAllNullFieldsOrPropertiesExcept("signingInformation");
	}
}
