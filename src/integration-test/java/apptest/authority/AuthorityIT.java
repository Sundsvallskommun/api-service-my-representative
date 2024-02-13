package apptest.authority;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.myrepresentative.MyRepresentatives;

import apptest.CommonStubs;

@WireMockAppTestSuite(files = "classpath:/Authorities/", classes = MyRepresentatives.class)
class AuthorityIT extends AbstractAppTest {

    @Test
    void test1_getAuthorities_shouldReturnCompleteResponse() {
        CommonStubs.stubAllTokens();
        setupCall()
                .withServicePath("/authorities?authorityIssuer.partyId=fb2f0290-3820-11ed-a261-0242ac120005&&authorityIssuer.type=orgnr&authorityAcquirer.partyId=ffaf692e-6686-11ed-9022-0242ac120002&authorityAcquirer.type=pnr&page=0&limit=100")
                .withHttpMethod(HttpMethod.GET)
                .withExpectedResponseStatus(HttpStatus.OK)
                .withExpectedResponse("expected.json")
                .sendRequestAndVerifyResponse();
    }

    @Test
    void test2_404FromParty_shouldThrow404() {
        CommonStubs.stubPartyToken();
        setupCall()
                .withServicePath("/authorities?authorityAcquirer.partyId=ffaf692e-6686-11ed-9022-0242ac120012&authorityAcquirer.type=pnr&page=0&limit=100")
                .withHttpMethod(HttpMethod.GET)
                .withExpectedResponseStatus(HttpStatus.NOT_FOUND)
                .withExpectedResponse("expected.json")
                .sendRequestAndVerifyResponse();
    }

    @Test
    void test3_noResponseFromMinaOmbud_shouldReturnEmptyResponse() {
        CommonStubs.stubAllTokens();
        setupCall()
                .withServicePath("/authorities?authorityAcquirer.partyId=ffaf692e-6686-11ed-9022-0242ac120002&authorityAcquirer.type=pnr&page=0&limit=100")
                .withHttpMethod(HttpMethod.GET)
                .withExpectedResponseStatus(HttpStatus.OK)
                .withExpectedResponse("expected.json")
                .sendRequestAndVerifyResponse();
    }
}
