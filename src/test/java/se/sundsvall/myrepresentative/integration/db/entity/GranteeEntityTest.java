package se.sundsvall.myrepresentative.integration.db.entity;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class GranteeEntityTest {

	private static final String ID = "id";
	private static final MandateEntity MANDATE = new MandateEntity();
	private static final String PARTY_ID = UUID.randomUUID().toString();

	@Test
	void testBean() {
		MatcherAssert.assertThat(GranteeEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilders() {
		final var entity = new GranteeEntity()
			.withId(ID)
			.withPartyId(PARTY_ID)
			.withMandate(MANDATE);

		assertBean(entity);
	}

	@Test
	void testSettersAndGetters() {
		final var entity = new GranteeEntity();
		entity.setId(ID);
		entity.setPartyId(PARTY_ID);
		entity.setMandate(MANDATE);

		assertBean(entity);
	}

	private void assertBean(final GranteeEntity entity) {
		assertThat(entity).isNotNull();
		assertThat(entity.getId()).isEqualTo(ID);
		assertThat(entity.getPartyId()).isEqualTo(PARTY_ID);
		assertThat(entity.getMandate()).isEqualTo(MANDATE);

		assertThat(entity).hasNoNullFieldsOrProperties();
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new GranteeEntity()).hasAllNullFieldsOrProperties();
	}
}
