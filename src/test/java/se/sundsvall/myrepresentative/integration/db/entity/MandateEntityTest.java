package se.sundsvall.myrepresentative.integration.db.entity;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.OffsetDateTime.now;
import static java.time.ZoneId.systemDefault;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.hamcrest.CoreMatchers.allOf;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.sundsvall.myrepresentative.api.model.MandateStatus;

class MandateEntityTest {

	private static final String ID = "id";
	private static final String NAME = "Ankeborgs Margarinfabrik";
	private static final String GRANTOR_PARTY_ID = UUID.randomUUID().toString();
	private static final String SIGNATORY_PARTY_ID = UUID.randomUUID().toString();
	private static final String MUNICIPALITY_ID = "2281";
	private static final OffsetDateTime CREATED = now();
	private static final OffsetDateTime UPDATED = now().plusDays(1);
	private static final OffsetDateTime VALID_FROM = now().minusDays(1);
	private static final OffsetDateTime VALID_TO = now().plusYears(1);
	private static final MandateStatus STATUS = MandateStatus.ACTIVE;
	private static final List<GranteeEntity> GRANTEES = List.of(new GranteeEntity(), new GranteeEntity());

	@BeforeAll
	static void beforeAll() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), OffsetDateTime.class);
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
		var entity = new MandateEntity()
			.withId(ID)
			.withName(NAME)
			.withGrantorPartyId(GRANTOR_PARTY_ID)
			.withSignatoryPartyId(SIGNATORY_PARTY_ID)
			.withMunicipalityId(MUNICIPALITY_ID)
			.withCreated(CREATED)
			.withUpdated(UPDATED)
			.withValidFrom(VALID_FROM)
			.withValidTo(VALID_TO)
			.withStatus(STATUS)
			.withGrantees(GRANTEES);

		assertBean(entity);
	}

	@Test
	void testSetterSndGetters() {
		final var entity = new MandateEntity();
		entity.setId(ID);
		entity.setName(NAME);
		entity.setGrantorPartyId(GRANTOR_PARTY_ID);
		entity.setSignatoryPartyId(SIGNATORY_PARTY_ID);
		entity.setMunicipalityId(MUNICIPALITY_ID);
		entity.setCreated(CREATED);
		entity.setUpdated(UPDATED);
		entity.setValidFrom(VALID_FROM);
		entity.setValidTo(VALID_TO);
		entity.setStatus(STATUS);
		entity.setGrantees(GRANTEES);

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
		assertThat(entity.getValidFrom()).isEqualTo(VALID_FROM);
		assertThat(entity.getValidTo()).isEqualTo(VALID_TO);
		assertThat(entity.getStatus()).isEqualTo(STATUS);
		assertThat(entity.getGrantees()).isEqualTo(GRANTEES);
		assertThat(entity).hasNoNullFieldsOrProperties();
	}

	@Test
	void testPrePersistWithEmpty() {
		final var entity = new MandateEntity();
		assertThat(entity.getCreated()).isNull();
		assertThat(entity.getUpdated()).isNull();
		assertThat(entity.getValidFrom()).isNull();
		assertThat(entity.getValidTo()).isNull();

		entity.onCreate();

		assertThat(entity.getCreated()).isNotNull().isCloseTo(now(systemDefault()), within(2, ChronoUnit.SECONDS));
		assertThat(entity.getUpdated()).isNotNull().isCloseTo(now(systemDefault()), within(2, ChronoUnit.SECONDS));
		assertThat(entity.getValidFrom()).isNotNull().isCloseTo(now(systemDefault()), within(2, ChronoUnit.SECONDS));
		assertThat(entity.getValidTo()).isNotNull().isCloseTo(now(systemDefault()).plusYears(2), within(2, ChronoUnit.SECONDS));
	}

	@Test
	void testPrePersistWithValidFromAndValidToAlreadySet() {
		final var entity = new MandateEntity()
			.withValidFrom(VALID_FROM)
			.withValidTo(VALID_TO);

		entity.onCreate();

		assertThat(entity.getValidFrom()).isNotNull().isEqualTo(VALID_FROM);
		assertThat(entity.getValidTo()).isNotNull().isEqualTo(VALID_TO);
	}

	@Test
	void testPreUpdate() {
		final var entity = new MandateEntity();
		assertThat(entity.getUpdated()).isNull();

		entity.onUpdate();

		assertThat(entity.getUpdated()).isNotNull().isCloseTo(now(systemDefault()), within(2, ChronoUnit.SECONDS));
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new MandateEntity()).hasAllNullFieldsOrProperties();
	}
}
