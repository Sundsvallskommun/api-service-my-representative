package apptest;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.myrepresentative.MyRepresentatives;
import se.sundsvall.myrepresentative.api.model.MandateStatus;

@WireMockAppTestSuite(files = "classpath:/SearchMandatesIT/", classes = MyRepresentatives.class)
@Sql({
	"/db/scripts/truncate.sql",
	"/db/scripts/testdata-it.sql"
})
class SearchMandatesIT extends AbstractAppTest {

	private static final String RESPONSE = "response.json";
	private static final String BASE_URL = "/2281/my_namespace/mandates";

	@Test
	void test01_searchAllMandates() {
		setupCall()
			.withServicePath(BASE_URL)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test02_searchMandates_withLimit() {
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance().replacePath(BASE_URL)
				.queryParam("page", "1")
				.queryParam("limit", "2")
				.toUriString())
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test03_searchMandates_withPageOffset() {
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance().replacePath(BASE_URL)
				.queryParam("page", "2")
				.queryParam("limit", "2")
				.toUriString())
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test04_searchMandates_withGrantor() {
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance().replacePath(BASE_URL)
				.queryParam("grantorPartyId", "e47aa4d3-c79a-4d08-a1d2-799ba549e0c4")
				.toUriString())
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test05_searchMandates_withGrantee() {
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance()
				.replacePath(BASE_URL)
				.queryParam("granteePartyId", "e47aa4d3-c79a-4d08-a1d2-799ba549e0c7")
				.toUriString()
			)
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test06_searchMandates_withSignatory() {
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance().replacePath(BASE_URL)
				.queryParam("signatoryPartyId", "e47aa4d3-c79a-4d08-a1d2-799ba549e0c2")
				.toUriString())
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test07_searchMandates_withAllParameters() {
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance().replacePath(BASE_URL)
				.queryParam("grantorPartyId", "e47aa4d3-c79a-4d08-a1d2-799ba549e0c5")
				.queryParam("granteePartyId", "e47aa4d3-c79a-4d08-a1d2-799ba549e0c7")
				.queryParam("signatoryPartyId", "e47aa4d3-c79a-4d08-a1d2-799ba549e0c6")
				.queryParam("statuses", List.of(MandateStatus.EXPIRED))
				.queryParam("page", "1")
				.queryParam("limit", "2")
				.toUriString())
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test08_searchMandates_withNoResults() {
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance().replacePath(BASE_URL)
				.queryParam("grantorPartyId", "c5a2f724-71d4-41f5-9993-1692407e9236")
				.toUriString())
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test09_searchMandates_shouldBeNamespaceAware() {
		// grantorPartyId exists, but not for the given namespace
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance().replacePath(BASE_URL.replace("my_namespace", "another_namespace"))
				.queryParam("grantorPartyId", "e47aa4d3-c79a-4d08-a1d2-799ba549e0c5")
				.toUriString())
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test10_searchMandates_shouldBeMunicipalityAware() {
		// grantorPartyId exists, but not for the given municipalityId
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance().replacePath(BASE_URL.replace("2281", "1984"))
				.queryParam("grantorPartyId", "e47aa4d3-c79a-4d08-a1d2-799ba549e0c5")
				.toUriString())
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}

	@Test
	void test11_searchMandates_Search_status_withNoResults() {
		setupCall()
			.withServicePath(UriComponentsBuilder.newInstance().replacePath(BASE_URL)
				.queryParam("statuses", List.of(MandateStatus.INACTIVE))
				.queryParam("page", "1")
				.queryParam("limit", "2")
				.toUriString())
			.withHttpMethod(GET)
			.withExpectedResponseStatus(OK)
			.withExpectedResponse(RESPONSE)
			.sendRequestAndVerifyResponse();
	}
}
