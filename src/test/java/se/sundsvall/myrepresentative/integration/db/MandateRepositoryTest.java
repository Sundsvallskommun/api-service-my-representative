package se.sundsvall.myrepresentative.integration.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.integration.db.entity.MandateEntity.NOT_DELETED;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@ActiveProfiles("junit")
@Sql(scripts = {
	"/db/scripts/truncate.sql",
	"/db/scripts/testdata-junit.sql"
})
class MandateRepositoryTest {

	@Autowired
	private MandateRepository mandateRepository;

	@Test
	void testFindActiveByIdAndMunicipalityIdAndNamespace_shouldOnlyReturnNotDeleted() {
		// Verify that we can fetch an active (not deleted) mandate.
		final var activeMandate = mandateRepository.findActiveByIdAndMunicipalityIdAndNamespace("358e6915-fba4-4070-9584-35423db53b66", MUNICIPALITY_ID, "MY_NAMESPACE");
		assertThat(activeMandate).isPresent();
		assertThat(activeMandate.get().getDeleted()).isEqualTo(NOT_DELETED);
	}

	@Test
	void testFindActiveByIdAndMunicipalityIdAndNamespace_shouldNotReturnDeleted() {
		// Fetch a deleted mandate directly and verify it is marked as deleted
		final var deletedMandate = mandateRepository.findByIdAndMunicipalityIdAndNamespace("3e2ae9d2-e18d-468e-b48d-5840f770edf9", MUNICIPALITY_ID, "MY_NAMESPACE");
		final var deletedAt = LocalDateTime.parse("2025-09-30T16:21:03.033748");    // The same as in the testdata-junit.sql

		assertThat(deletedMandate).isPresent();
		assertThat(deletedMandate.get().getDeleted()).isNotEqualTo(NOT_DELETED);
		assertThat(LocalDateTime.parse(deletedMandate.get().getDeleted())).isEqualTo(deletedAt);

		// Try to fetch the deleted mandate using the convenience method and verify that it is not returned
		final var shouldNotFindDeletedMandate = mandateRepository.findActiveByIdAndMunicipalityIdAndNamespace("3e2ae9d2-e18d-468e-b48d-5840f770edf9", MUNICIPALITY_ID, "MY_NAMESPACE");
		assertThat(shouldNotFindDeletedMandate).isNotPresent();
	}

	@Test
	void testFindByIdAndMunicipalityIdAndNamespaceAndDeletedIs() {
		// Fetch an active (not deleted) mandate and verify that we can fetch a mandate that's not deleted.
		final var activeMandate = mandateRepository.findByIdAndMunicipalityIdAndNamespaceAndDeletedIs("358e6915-fba4-4070-9584-35423db53b66", MUNICIPALITY_ID, "MY_NAMESPACE", NOT_DELETED);
		assertThat(activeMandate).isPresent();
		assertThat(activeMandate.get().getDeleted()).isEqualTo(NOT_DELETED);
	}
}
