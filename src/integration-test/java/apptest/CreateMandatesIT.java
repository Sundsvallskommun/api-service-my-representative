package apptest;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.myrepresentative.MyRepresentatives;

@WireMockAppTestSuite(files = "classpath:/CreateMandateIT/", classes = MyRepresentatives.class)
@Sql({
	"/db/scripts/truncate.sql",
	"/db/scripts/testdata-it.sql"
})
class CreateMandatesIT extends AbstractAppTest {

	private static final String RESPONSE = "response.json";
	private static final String REQUEST = "request.json";
	private static final String BASE_URL = "/2281/my_namespace/mandates";

	@Test
	void test01_createMandate() {
		final var headers = setupCall()
			.withServicePath(BASE_URL)
			.withHttpMethod(POST)
			.withRequest(REQUEST)
			.withExpectedResponseStatus(CREATED)
			.withExpectedResponseHeader(LOCATION, List.of(BASE_URL + "/([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})$"))
			.sendRequest()
			.getResponseHeaders();

		// Use the Location header to get the created mandate and verify it was created
		final var location = headers.getFirst(LOCATION);

		//Read the created mandate to verify it was created
		setupCall()
			.withServicePath(location)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	/**
	 * A test where the same mandate details already exists and an overlapping time period, should fail.
	 * "id" in testdata-it.sql is 24b59fba-c6c4-4cec-8723-7d4feb062257
	 */
	@Test
	void test02_createMandate_mandateAlreadyExists() {
		setupCall()
			.withServicePath(BASE_URL)
			.withHttpMethod(POST)
			.withRequest(REQUEST)
			.withExpectedResponseStatus(CONFLICT)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	/**
	 * A test where the same mandate details already exists but with a different time period, should pass.
	 */
	@Test
	void test03_createMandate_sameMandateDifferentTimePeriod() {
		final var headers = setupCall()
			.withServicePath(BASE_URL)
			.withHttpMethod(POST)
			.withRequest(REQUEST)
			.withExpectedResponseStatus(CREATED)
			.withExpectedResponseHeader(LOCATION, List.of(BASE_URL + "/([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})$"))
			.sendRequest()
			.getResponseHeaders();

		// Use the Location header to get the created mandate and verify it was created
		final var location = headers.getFirst(LOCATION);

		//Read the created mandate to verify it was created
		setupCall()
			.withServicePath(location)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	/**
	 * Creates a new mandate as one that already exists but with different granteePartyId, should pass.
	 */
	@Test
	void test04_createMandate_differentGrantee() {
		final var headers = setupCall()
			.withServicePath(BASE_URL)
			.withHttpMethod(POST)
			.withRequest(REQUEST)
			.withExpectedResponseStatus(CREATED)
			.withExpectedResponseHeader(LOCATION, List.of(BASE_URL + "/([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})$"))
			.sendRequest()
			.getResponseHeaders();

		// Use the Location header to get the created mandate and verify it was created
		final var location = headers.getFirst(LOCATION);

		//Read the created mandate to verify it was created
		setupCall()
			.withServicePath(location)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	/**
	 * Creates a new mandate as one that already exists for the organization, but with different signatoryPartyId
	 * Should not pass.
	 */
	@Test
	void test05_createMandate_differentSignatory() {
		setupCall()
			.withServicePath(BASE_URL)
			.withHttpMethod(POST)
			.withRequest(REQUEST)
			.withExpectedResponseStatus(CONFLICT)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test06_createMandate_differentMunicipalityAndNamespace() {
		final var path = "/1984/other_namespace/mandates";
		final var headers = setupCall()
			.withServicePath(path)
			.withHttpMethod(POST)
			.withRequest(REQUEST)
			.withExpectedResponseStatus(CREATED)
			.withExpectedResponseHeader(LOCATION, List.of(path + "/([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})$"))
			.sendRequest()
			.getResponseHeaders();

		// Use the Location header to get the created mandate and verify it was created
		final var location = headers.getFirst(LOCATION);

		//Read the created mandate to verify it was created
		setupCall()
			.withServicePath(location)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	/**
	 * The signatory is not an authorized signatory for the organization, should fail.
	 */
	@Test
	void test07_createMandate_notAuthorizedSignatory() {
		setupCall()
			.withServicePath(BASE_URL)
			.withHttpMethod(POST)
			.withRequest(REQUEST)
			.withExpectedResponseStatus(FORBIDDEN)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	/**
	 * The person does not have any engagement with the organization, should fail.
	 */
	@Test
	void test08_createMandate_organizationNotFound() {
		setupCall()
			.withServicePath(BASE_URL)
			.withHttpMethod(POST)
			.withRequest(REQUEST)
			.withExpectedResponseStatus(NOT_FOUND)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}
}
