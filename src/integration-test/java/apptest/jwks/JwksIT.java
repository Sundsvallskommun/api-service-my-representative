package apptest.jwks;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.myrepresentative.MyRepresentatives;

@WireMockAppTestSuite(files = "classpath:/Jwks/", classes = MyRepresentatives.class)
@Sql({
	"/db/scriptsIT/truncate.sql",
	"/db/scriptsIT/testdata.sql"
})
public class JwksIT extends AbstractAppTest {

	@Test
	void test001_getJwks() {
		setupCall()
			.withServicePath("/jwks")
			.withHttpMethod(HttpMethod.GET)
			.withExpectedResponseStatus(HttpStatus.OK)
			.withExpectedResponse("expected.json")
			.sendRequestAndVerifyResponse();
	}
}
