package apptest.mandates;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.myrepresentative.MyRepresentatives;
import apptest.CommonStubs;

@WireMockAppTestSuite(files = "classpath:/Mandates/", classes = MyRepresentatives.class)
@Sql({
    "/db/scriptsIT/truncate.sql",
    "/db/scriptsIT/testdata.sql"
})
class MandatesIT extends AbstractAppTest {

    @Test
    void test001_getMandates_shouldReturnCompleteResponse() {

        CommonStubs.stubAllTokens();
        setupCall()
            .withExtensions(new JwtRequestMatchExtension(false))
            .withServicePath("/mandates?mandateIssuer.partyId=9082c663-d38c-439b-aa43-c04aa39d5fe4&mandateIssuer.type=orgnr&mandateAcquirer.partyId=16df33c8-73af-4be6-ac3a-154306516789&mandateAcquirer.type=pnr&page=0&limit=100")
            .withHttpMethod(HttpMethod.GET)
            .withExpectedResponseStatus(HttpStatus.OK)
            .withExpectedResponse("expected.json")
            .sendRequestAndVerifyResponse();
    }

    @Test
    void test002_404FromParty_shouldThrow404() {
        CommonStubs.stubPartyToken();
        setupCall()
            .withServicePath("/mandates?mandateAcquirer.partyId=ffaf692e-6686-11ed-9022-0242ac120012&mandateAcquirer.type=pnr&page=0&limit=100")
            .withHttpMethod(HttpMethod.GET)
            .withExpectedResponseStatus(HttpStatus.NOT_FOUND)
            .withExpectedResponse("expected.json")
            .sendRequestAndVerifyResponse();
    }

    @Test
    void test003_noResponseFromMinaOmbud_shouldReturnEmptyResponse() {
        CommonStubs.stubAllTokens();
        setupCall()
            .withExtensions(new JwtRequestMatchExtension(false))
            .withServicePath("/mandates?mandateAcquirer.partyId=ffaf692e-6686-11ed-9022-0242ac120002&mandateAcquirer.type=pnr&page=0&limit=100")
            .withHttpMethod(HttpMethod.GET)
            .withExpectedResponseStatus(HttpStatus.OK)
            .withExpectedResponse("expected.json")
            .sendRequestAndVerifyResponse();
    }
}
