package se.sundsvall.myrepresentative.integration.db.entity;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.LocalDateTime.now;
import static java.time.ZoneId.systemDefault;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.hamcrest.CoreMatchers.allOf;
import static se.sundsvall.myrepresentative.TestObjectFactory.ACTIVE_FROM;
import static se.sundsvall.myrepresentative.TestObjectFactory.CREATED;
import static se.sundsvall.myrepresentative.TestObjectFactory.DELETED;
import static se.sundsvall.myrepresentative.TestObjectFactory.GRANTEE_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.GRANTOR_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.INACTIVE_AFTER;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAME;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;
import static se.sundsvall.myrepresentative.TestObjectFactory.SIGNATORY_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.STATUS;
import static se.sundsvall.myrepresentative.TestObjectFactory.UPDATED;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class MandateEntityTest {

	@BeforeAll
	static void beforeAll() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDateTime.class);
		registerValueGenerator(() -> LocalDate.now().plusDays(new Random().nextInt()), LocalDate.class);
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(MandateEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
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
			.withDeleted(DELETED)
			.withGrantee(GRANTEE_PARTY_ID)
			.withNamespace(NAMESPACE)
			.withStatus(STATUS);

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
		entity.setDeleted(DELETED);
		entity.setGranteePartyId(GRANTEE_PARTY_ID);
		entity.setNamespace(NAMESPACE);
		entity.setStatus(STATUS);

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
		assertThat(entity.getDeleted()).isEqualTo(DELETED);
		assertThat(entity.getGranteePartyId()).isEqualTo(GRANTEE_PARTY_ID);
		assertThat(entity.getStatus()).isEqualTo(STATUS);
		assertThat(entity).hasNoNullFieldsOrProperties();
	}

	@Test
	void testPrePersist() {
		final var entity = new MandateEntity();
		assertThat(entity.getCreated()).isNull();
		assertThat(entity.getUpdated()).isNull();

		entity.onCreate();

		assertThat(entity.getCreated()).isNotNull().isCloseTo(now(systemDefault()), within(2, ChronoUnit.SECONDS));
		assertThat(entity.getUpdated()).isNotNull().isCloseTo(now(systemDefault()), within(2, ChronoUnit.SECONDS));
	}

	@Test
	void testPreUpdate() {
		final var preUpdate = LocalDateTime.now().minusSeconds(1);
		final var entity = new MandateEntity();
		entity.setUpdated(preUpdate);

		entity.onUpdate();

		assertThat(entity.getUpdated()).isNotNull().isAfter(preUpdate);
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new MandateEntity()).hasAllNullFieldsOrPropertiesExcept("deleted");
	}
}
