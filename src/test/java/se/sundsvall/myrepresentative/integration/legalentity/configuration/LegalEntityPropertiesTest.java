package se.sundsvall.myrepresentative.integration.legalentity.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import se.sundsvall.myrepresentative.MyRepresentatives;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MyRepresentatives.class)
@ActiveProfiles("junit")
class LegalEntityPropertiesTest {

	@Autowired
	private LegalEntityProperties properties;

	@Test
	void testProperties() {
		assertThat(properties.connectTimeout()).isEqualTo(5);
		assertThat(properties.readTimeout()).isEqualTo(30);
		assertThat(properties.validationEnabled()).isTrue();
	}
}
