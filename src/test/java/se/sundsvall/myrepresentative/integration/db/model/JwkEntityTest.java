package se.sundsvall.myrepresentative.integration.db.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static java.time.OffsetDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;

import com.google.code.beanmatchers.BeanMatchers;
import java.time.OffsetDateTime;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class JwkEntityTest {

	private static final Random RANDOM = new Random();

	@BeforeAll
	static void setup() {
		BeanMatchers.registerValueGenerator(() -> now().plusDays(RANDOM.nextInt()), OffsetDateTime.class);
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

		var created = now().plusDays(RANDOM.nextInt());
		var validUntil = now().plusDays(RANDOM.nextInt());
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
