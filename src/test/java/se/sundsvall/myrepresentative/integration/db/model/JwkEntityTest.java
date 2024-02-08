package se.sundsvall.myrepresentative.integration.db.model;

import com.google.code.beanmatchers.BeanMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Random;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;

class JwkEntityTest {

	@BeforeAll
	static void setup() {
		BeanMatchers.registerValueGenerator(() -> now().plusDays(new Random().nextInt()), OffsetDateTime.class);
	}

	@Test
	void testBean() {
		assertThat(JwkEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilder() {

		var created = now().plusDays(new Random().nextInt());
		var validUntil = now().plusDays(new Random().nextInt());
		var jwkBody = "jwk";
		var id = 123L;

		var result = JwkEntity.builder()
			.withId(id)
			.withJwkJson(jwkBody)
			.withCreated(created)
			.withValidUntil(validUntil)
			.build();

		assertThat(result.getId()).isEqualTo(id);
		assertThat(result.getJwkJson()).isEqualTo(jwkBody);
		assertThat(result.getCreated()).isEqualTo(created);
		assertThat(result.getValidUntil()).isEqualTo(validUntil);
	}
}