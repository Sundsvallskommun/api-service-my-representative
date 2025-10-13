package apptest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.myrepresentative.MyRepresentatives;
import se.sundsvall.myrepresentative.api.model.MandateStatus;
import se.sundsvall.myrepresentative.integration.db.MandateRepository;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;

@WireMockAppTestSuite(files = "classpath:/DeleteMandatesIT/", classes = MyRepresentatives.class)
@Sql({
	"/db/scripts/truncate.sql",
	"/db/scripts/testdata-it.sql"
})
class DeleteMandatesIT extends AbstractAppTest {
	
	private static final String BASE_URL = "/2281/my_namespace/mandates";
	private static final String MUNICIPALITY_ID = "2281";
	private static final String NAMESPACE = "my_namespace";
	
	@Autowired
	private MandateRepository mandateRepository;
	
	@Test
	void test01_deleteMandate() {
		final var idToDelete = "24b59fba-c6c4-4cec-8723-7d4feb062257";
		
		// Verify that we have a mandate with ACTIVE status before we delete it
		final var entity = getEntity(idToDelete);
		
		assertThat(entity.getStatus()).isEqualTo(MandateStatus.ACTIVE.toString());
		
		// Delete it
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance()
				.replacePath(BASE_URL + "/" + idToDelete)
				.toUriString())
			.withHttpMethod(DELETE)
			.withExpectedResponseStatus(NO_CONTENT)
			.sendRequest();
		
		// Verify that the mandate status is now set to DELETED
		final var deletedEntity = getEntity(idToDelete);
		assertThat(deletedEntity.getStatus()).isEqualTo(MandateStatus.DELETED.toString());
	}
	
	@Test
	void test02_deleteMandate_mandateDoesNotExist() {
		final var idToDelete = "00000000-0000-0000-0000-000000000000";
		
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance()
				.replacePath(BASE_URL + "/" + idToDelete)
				.toUriString())
			.withHttpMethod(DELETE)
			.withExpectedResponseStatus(NO_CONTENT)
			.sendRequest();
		
		// Verify that the mandate does not exist in the database
		final var entity = mandateRepository.findByMunicipalityIdAndNamespaceAndId("2281", "my_namespace", idToDelete);
		assertThat(entity).isEmpty();
	}
	
	@Test
	void test03_deleteMandate_mandateAlreadyDeleted() {
		final var idToDelete = "62c07c65-a03e-44c4-8505-a39b046bd6d6"; // This mandate is already deleted in testdata-it.sql

		// Verify that we have the mandate with DELETED status before we try to delete it
		final var entity = getEntity(idToDelete);
		
		assertThat(entity.getStatus()).isEqualTo(MandateStatus.DELETED.toString());
		
		// Try to delete it
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance()
				.replacePath(BASE_URL + "/" + idToDelete)
				.toUriString())
			.withHttpMethod(DELETE)
			.withExpectedResponseStatus(NO_CONTENT)
			.sendRequest();
		
		// Verify that the mandate is the same as before
		final var oldEntity = getEntity(idToDelete);
		
		assertThat(entity).isEqualTo(oldEntity);
	}

	/**
	 * Verify that we don't accidentally delete a mandate for another municipalityId even though the id exists.
	 */
	@Test
	void test04_deleteMandateForAnotherMunicipalityId() {
		final var idToDelete = "24b59fba-c6c4-4cec-8723-7d4feb062257";

		// Verify that we have a mandate with ACTIVE status for a municipality, namespace and id before we try to delete it
		final var entity = getEntity(idToDelete);

		assertThat(entity.getStatus()).isEqualTo(MandateStatus.ACTIVE.toString());

		// Try to delete it
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance()
				.replacePath(BASE_URL.replace(MUNICIPALITY_ID, "1984") + "/" + idToDelete) // Different municipalityId
				.toUriString())
			.withHttpMethod(DELETE)
			.withExpectedResponseStatus(NO_CONTENT)
			.sendRequest();

		// Verify that the mandate status is still active
		final var deletedEntity = getEntity(idToDelete);
		assertThat(deletedEntity.getStatus()).isEqualTo(MandateStatus.ACTIVE.toString());
	}

	/**
	 * Verify that we don't accidentally delete a mandate for another namespace even though the id exists.
	 */
	@Test
	void test05_deleteMandateForAnotherNamespace() {
		final var idToDelete = "24b59fba-c6c4-4cec-8723-7d4feb062257";

		// Verify that we have a mandate with ACTIVE status for a municipality, namespace and id before we try to delete it
		final var entity = getEntity(idToDelete);

		assertThat(entity.getStatus()).isEqualTo(MandateStatus.ACTIVE.toString());

		// Try to delete it
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance()
				.replacePath(BASE_URL.replace(NAMESPACE, "another_namespace") + "/" + idToDelete) // Different namespace
				.toUriString())
			.withHttpMethod(DELETE)
			.withExpectedResponseStatus(NO_CONTENT)
			.sendRequest();

		// Verify that the mandate status is still active
		final var deletedEntity = getEntity(idToDelete);
		assertThat(deletedEntity.getStatus()).isEqualTo(MandateStatus.ACTIVE.toString());
	}

	private MandateEntity getEntity(String id) {
		return mandateRepository.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, id)
			.orElseThrow(() -> new AssertionError("Mandate with id " + id + " was not found in the database for the given municipalityId and namespace"));
	}
}
