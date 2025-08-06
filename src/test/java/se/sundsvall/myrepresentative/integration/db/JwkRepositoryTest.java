package se.sundsvall.myrepresentative.integration.db;

import static java.time.OffsetDateTime.now;
import static java.time.temporal.ChronoUnit.MICROS;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import se.sundsvall.myrepresentative.integration.db.model.JwkEntity;

@SpringBootTest
@ActiveProfiles("junit")
@Sql({
	"/db/scripts/truncate.sql",
	"/db/scripts/testdata.sql"
})
@DirtiesContext
class JwkRepositoryTest {

	private static final Random RANDOM = new Random();

	@Autowired
	private JwkRepository repository;

	@Test
	void create() {
		var jwk = "body";
		var validUntil = now().plusDays(RANDOM.nextInt(100));
		var jwkEntity = JwkEntity.builder()
			.withJwkJson(jwk)
			.withValidUntil(validUntil)
			.build();

		var id = repository.saveAndFlush(jwkEntity).getId();
		var result = repository.findById(id).orElseThrow(() -> new RuntimeException("Missing test data"));

		assertThat(result.getJwkJson()).isEqualTo(jwk);
		assertThat(result.getCreated()).isCloseTo(now(), within(2, SECONDS));
		assertThat(result.getValidUntil()).isEqualTo(validUntil.truncatedTo(MICROS));
	}

	@Test
	void read() {
		var jwk = "jwk_body_100";
		var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneId.systemDefault());
		var created = ZonedDateTime.parse("2024-02-05 12:14:32.234", formatter).toOffsetDateTime();
		var validUntil = ZonedDateTime.parse("2024-02-05 13:14:32.234", formatter).toOffsetDateTime();

		var result = repository.findById(100L).orElseThrow(() -> new RuntimeException("Missing test data"));

		assertThat(result.getJwkJson()).isEqualTo(jwk);
		assertThat(result.getCreated()).isEqualTo(created);
		assertThat(result.getValidUntil()).isEqualTo(validUntil);
	}

	@Test
	void update() {
		var entity = repository.findById(100L).orElseThrow(() -> new RuntimeException("Missing test data"));
		entity.setJwkJson("new_jwk_body");
		repository.saveAndFlush(entity);

		var result = repository.findById(100L).orElseThrow(() -> new RuntimeException("Missing test data"));

		var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneId.systemDefault());
		var created = ZonedDateTime.parse("2024-02-05 12:14:32.234", formatter).toOffsetDateTime();
		var validUntil = ZonedDateTime.parse("2024-02-05 13:14:32.234", formatter).toOffsetDateTime();

		assertThat(result.getJwkJson()).isEqualTo("new_jwk_body");
		assertThat(result.getCreated()).isEqualTo(created);
		assertThat(result.getValidUntil()).isEqualTo(validUntil);
	}

	@Test
	void delete() {
		assertThat(repository.findById(100L)).isPresent();

		repository.deleteById(100L);
		repository.flush();

		assertThat(repository.findById(100L)).isNotPresent();
	}

	@Test
	@Transactional
	void deleteByValidUntilBefore() {
		assertThat(repository.findById(100L)).isPresent();

		var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS").withZone(ZoneId.systemDefault());
		var dateTime = ZonedDateTime.parse("2024-02-05 13:14:32.234", formatter).toOffsetDateTime();

		repository.deleteByValidUntilBefore(dateTime);
		repository.flush();
		assertThat(repository.findById(100L)).isPresent();

		repository.deleteByValidUntilBefore(dateTime.plus(1, MILLIS));
		repository.flush();

		assertThat(repository.findById(100L)).isNotPresent();

	}

	@Test
	void existsByValidUntilAfter() {
		assertThat(repository.findAll()).hasSize(1);

		var jwk = "body";
		var validUntil = now().plusMinutes(1);
		var jwkEntity = JwkEntity.builder()
			.withJwkJson(jwk)
			.withValidUntil(validUntil)
			.build();

		repository.saveAndFlush(jwkEntity);

		assertThat(repository.findAll()).hasSize(2);
	}

	@Test
	void findByMunicipalityIdAndValidUntilAfter() {
		var jwk = "body";
		var validUntil = now().plusMinutes(1);
		var jwkEntity = JwkEntity.builder()
			.withJwkJson(jwk)
			.withValidUntil(validUntil)
			.withMunicipalityId(MUNICIPALITY_ID)
			.build();

		repository.saveAndFlush(jwkEntity);

		assertThat(repository.findByMunicipalityIdAndValidUntilAfter(MUNICIPALITY_ID, now()))
			.filteredOn(entity -> entity.getJwkJson().equals("body"))
			.hasSize(1);
	}
}
