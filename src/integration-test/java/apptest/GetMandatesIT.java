package apptest;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.elasticsearch.DataElasticsearchTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.myrepresentative.MyRepresentatives;

@WireMockAppTestSuite(files = "classpath:/GetMandateIT/", classes = MyRepresentatives.class)
@Sql({
	"/db/scripts/truncate.sql",
	"/db/scripts/testdata-it.sql"
})
class GetMandatesIT extends AbstractAppTest {

	private static final String RESPONSE = "response.json";
	private static final String BASE_URL = "/2281/my_namespace/mandates";
	private static final String MUNICIPALITY_ID = "2281";
	private static final String NAMESPACE = "my_namespace";
	
	@Test
	void test01_getMandateWithBankIdSigningInformation() {
		final var idToGet = "62c07c65-a03e-44c4-8505-a39b046bd6d6";
		
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance()
				.replacePath(BASE_URL + "/" + idToGet)
				.toUriString())
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}
	
	@Test
	void test02_getMandateNotFound() {
		final var idToGet = "00000000-0000-0000-0000-000000000000";
		
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance()
				.replacePath(BASE_URL + "/" + idToGet)
				.toUriString())
			.withHttpMethod(GET)
			.withExpectedResponseStatus(NOT_FOUND)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	/**
	 * Verify we don't accidentally get a mandate from another municipality even though the id exists.
	 */
	@Test
	void test03_getMandateForAnotherMunicipality() {
		final var idToGet = "62c07c65-a03e-44c4-8505-a39b046bd6d6";

		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance()
				.replacePath(BASE_URL.replace(MUNICIPALITY_ID, "1984") + "/" + idToGet)
				.toUriString())
			.withHttpMethod(GET)
			.withExpectedResponseStatus(NOT_FOUND)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	/**
	 * Verify we don't accidentally get a mandate from another namespace even though the id exists.
	 */
	@Test
	void test04_getMandateForAnotherNamespace() {
		final var idToGet = "62c07c65-a03e-44c4-8505-a39b046bd6d6";

		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance()
				.replacePath(BASE_URL.replace(NAMESPACE, "another_namespace") + "/" + idToGet)
				.toUriString())
			.withHttpMethod(GET)
			.withExpectedResponseStatus(NOT_FOUND)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}
}
