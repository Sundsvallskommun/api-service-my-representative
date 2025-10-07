package se.sundsvall.myrepresentative.integration.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.integration.db.entity.MandateEntity.NOT_DELETED;

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
		final var activeMandate = mandateRepository.findActiveByIdAndMunicipalityIdAndNamespace(
			"1a8f4c69-d6b7-4fcb-aa0b-74ed3a2e84a0", MUNICIPALITY_ID, "MY_NAMESPACE");
		assertThat(activeMandate).isPresent();
		assertThat(activeMandate.get().getDeleted()).isEqualTo(NOT_DELETED);
	}

	@Test
	void testFindActiveByIdAndMunicipalityIdAndNamespace_shouldNotReturnDeleted() {
		// Fetch a deleted mandate directly and verify it is marked as deleted
		final var deletedMandate = mandateRepository.findByIdAndMunicipalityIdAndNamespace(
			"4f0c18c2-100a-4cb6-a127-e373dc629407", MUNICIPALITY_ID, "MY_NAMESPACE");

		assertThat(deletedMandate).isPresent();
		assertThat(deletedMandate.get().getDeleted()).isNotEqualTo(NOT_DELETED);

		// Try to fetch the deleted mandate using the convenience method and verify that it is not returned
		final var shouldNotFindDeletedMandate = mandateRepository.findActiveByIdAndMunicipalityIdAndNamespace(
			"4f0c18c2-100a-4cb6-a127-e373dc629407", MUNICIPALITY_ID, "MY_NAMESPACE");
		assertThat(shouldNotFindDeletedMandate).isNotPresent();
	}
}
