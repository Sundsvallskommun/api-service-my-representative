package apptest;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.myrepresentative.MyRepresentatives;

@WireMockAppTestSuite(files = "classpath:/WhitelistIT/", classes = MyRepresentatives.class)
@Sql({
	"/db/scripts/truncate.sql",
	"/db/scripts/testdata-it.sql"
})
class WhitelistIT extends AbstractAppTest {

	private static final String RESPONSE = "response.json";
	private static final String REQUEST = "request.json";
	private static final String BASE_URL = "/2281/my_namespace/mandates";
	private static final String VALIDATION_EXPRESSION = "/([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})$";

	@Test
	void test01_createMandateWhitelistedPair() {
		final var headers = setupCall()
			.withServicePath(BASE_URL)
			.withHttpMethod(POST)
			.withRequest(REQUEST)
			.withExpectedResponseStatus(CREATED)
			.withExpectedResponseHeader(LOCATION, List.of(BASE_URL + VALIDATION_EXPRESSION))
			.sendRequest()
			.getResponseHeaders();

		final var location = headers.getFirst(LOCATION);

		setupCall()
			.withServicePath(location)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_createMandateNonWhitelistedSignatory() {
		final var headers = setupCall()
			.withServicePath(BASE_URL)
			.withHttpMethod(POST)
			.withRequest(REQUEST)
			.withExpectedResponseStatus(CREATED)
			.withExpectedResponseHeader(LOCATION, List.of(BASE_URL + VALIDATION_EXPRESSION))
			.sendRequest()
			.getResponseHeaders();

		final var location = headers.getFirst(LOCATION);

		setupCall()
			.withServicePath(location)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test03_createMandateWhitelistedSignatoryDifferentGrantor() {
		final var headers = setupCall()
			.withServicePath(BASE_URL)
			.withHttpMethod(POST)
			.withRequest(REQUEST)
			.withExpectedResponseStatus(CREATED)
			.withExpectedResponseHeader(LOCATION, List.of(BASE_URL + VALIDATION_EXPRESSION))
			.sendRequest()
			.getResponseHeaders();

		final var location = headers.getFirst(LOCATION);

		setupCall()
			.withServicePath(location)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test04_createMandateMultipleGrantorsInWhitelist() {
		final var headers = setupCall()
			.withServicePath(BASE_URL)
			.withHttpMethod(POST)
			.withRequest(REQUEST)
			.withExpectedResponseStatus(CREATED)
			.withExpectedResponseHeader(LOCATION, List.of(BASE_URL + VALIDATION_EXPRESSION))
			.sendRequest()
			.getResponseHeaders();

		final var location = headers.getFirst(LOCATION);

		setupCall()
			.withServicePath(location)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}
}
