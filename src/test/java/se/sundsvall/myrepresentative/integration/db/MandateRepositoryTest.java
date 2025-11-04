package se.sundsvall.myrepresentative.integration.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.integration.db.entity.MandateEntity.NOT_DELETED;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import se.sundsvall.myrepresentative.api.model.MandateStatus;
import se.sundsvall.myrepresentative.api.model.SearchMandateParameters;

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

	public static Stream<Arguments> specificationProvider() {
		return Stream.of(
			Arguments.of("only grantor", createSearchMandateParameters("fb2f0290-3820-11ed-a261-0242ac120002", null, null, null), "1a8f4c69-d6b7-4fcb-aa0b-74ed3a2e84a0", 1),
			Arguments.of("only grantee", createSearchMandateParameters(null, "fb2f0290-3820-11ed-a261-0242ac120004", null, null), "1a8f4c69-d6b7-4fcb-aa0b-74ed3a2e84a0", 1),
			Arguments.of("only signatory", createSearchMandateParameters(null, null, "fb2f0290-3820-11ed-a261-0242ac120003", null), "1a8f4c69-d6b7-4fcb-aa0b-74ed3a2e84a0", 1),
			Arguments.of("everyone!", createSearchMandateParameters("fb2f0290-3820-11ed-a261-0242ac120005", "fb2f0290-3820-11ed-a261-0242ac120007", "fb2f0290-3820-11ed-a261-0242ac120006", null), "60850465-28d4-4caa-8e9d-69187461cb27", 2),
			Arguments.of("everyone and statues!", createSearchMandateParameters("fb2f0290-3820-11ed-a261-0242ac120005", "fb2f0290-3820-11ed-a261-0242ac120007", "fb2f0290-3820-11ed-a261-0242ac120006", List.of(MandateStatus.ACTIVE.name())),
				"60850465-28d4-4caa-8e9d-69187461cb27", 1),
			Arguments.of("only statuses!", createSearchMandateParameters(null, null, null, List.of(MandateStatus.DELETED.name())), "4f0c18c2-100a-4cb6-a127-e373dc629407", 1));
	}

	private static SearchMandateParameters createSearchMandateParameters(final String grantorPartyId, final String granteePartyId, final String signatoryPartyId, final List<String> statuses) {
		return new SearchMandateParameters()
			.withGrantorPartyId(grantorPartyId)
			.withGranteePartyId(granteePartyId)
			.withSignatoryPartyId(signatoryPartyId)
			.withStatuses(statuses)
			.withPage(1)
			.withLimit(15);
	}

	@Test
	void testFindActiveByIdAndMunicipalityIdAndNamespace_shouldOnlyReturnNotDeleted() {
		// Verify that we can fetch an active (not deleted) mandate.
		final var activeMandate = mandateRepository.findActiveByMunicipalityIdAndNamespaceAndId(
			MUNICIPALITY_ID, "MY_NAMESPACE", "1a8f4c69-d6b7-4fcb-aa0b-74ed3a2e84a0");
		assertThat(activeMandate).isPresent();
		assertThat(activeMandate.get().getDeleted()).isEqualTo(NOT_DELETED);
	}

	@Test
	void testFindActiveByIdAndMunicipalityIdAndNamespace_shouldNotReturnDeleted() {
		// Fetch a deleted mandate directly and verify it is marked as deleted
		final var deletedMandate = mandateRepository.findByMunicipalityIdAndNamespaceAndId(
			MUNICIPALITY_ID, "MY_NAMESPACE", "4f0c18c2-100a-4cb6-a127-e373dc629407");

		assertThat(deletedMandate).isPresent();
		assertThat(deletedMandate.get().getDeleted()).isNotEqualTo(NOT_DELETED);

		// Try to fetch the deleted mandate using the convenience method and verify that it is not returned
		final var shouldNotFindDeletedMandate = mandateRepository.findActiveByMunicipalityIdAndNamespaceAndId(
			MUNICIPALITY_ID, "MY_NAMESPACE", "4f0c18c2-100a-4cb6-a127-e373dc629407");
		assertThat(shouldNotFindDeletedMandate).isNotPresent();
	}

	@Test
	void testFindAllWithEmptySpecifications() {
		final var pageable = PageRequest.of(1, 10);
		final var mandates = mandateRepository.findAllWithParameters(null, null, new SearchMandateParameters(), pageable);
		assertThat(mandates).isNotNull();
		assertThat(mandates.getContent()).hasSize(3);
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("specificationProvider")
	void testFindAllWithSpecifications(final String testName, final SearchMandateParameters parameters, final String wantedId, final int expectedSize) {
		final var pageable = PageRequest.of(parameters.getPage(), parameters.getLimit());
		final var mandates = mandateRepository.findAllWithParameters(MUNICIPALITY_ID, "MY_NAMESPACE", parameters, pageable);
		assertThat(mandates).isNotNull().hasSize(expectedSize);
		assertThat(mandates.getContent().getFirst().getId()).isEqualTo(wantedId);

	}
}
