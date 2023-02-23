package apptest.mandates;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import se.sundsvall.dept44.test.AbstractAppTest;
import se.sundsvall.dept44.test.annotation.wiremock.WireMockAppTestSuite;
import se.sundsvall.myrepresentative.MyRepresentatives;

import apptest.CommonStubs;

@WireMockAppTestSuite(files = "classpath:/Mandates/", classes = {MyRepresentatives.class})
class MandatesIT extends AbstractAppTest {

    @Test
    void test1_getMandates_shouldReturnCompleteResponse() {
        CommonStubs.stubAllTokens();
        setupCall()
                .withServicePath("/getMandates?mandateIssuer.partyId=fb2f0290-3820-11ed-a261-0242ac120005&&mandateIssuer.type=orgnr&mandateAcquirer.partyId=ffaf692e-6686-11ed-9022-0242ac120002&mandateAcquirer.type=pnr&page=0&limit=100")
                .withHttpMethod(HttpMethod.GET)
                .withExpectedResponseStatus(HttpStatus.OK)
                .withExpectedResponse("expected.json")
                .sendRequestAndVerifyResponse();
    }

    @Test
    void test2_404FromParty_shouldThrow404() {
        CommonStubs.stubPartyToken();
        setupCall()
                .withServicePath("/getMandates?mandateAcquirer.partyId=ffaf692e-6686-11ed-9022-0242ac120012&mandateAcquirer.type=pnr&page=0&limit=100")
                .withHttpMethod(HttpMethod.GET)
                .withExpectedResponseStatus(HttpStatus.NOT_FOUND)
                .withExpectedResponse("expected.json")
                .sendRequestAndVerifyResponse();
    }

    @Test
    void test3_noResponseFromMinaOmbud_shouldReturnEmptyResponse() {
        CommonStubs.stubAllTokens();
        setupCall()
                .withServicePath("/getMandates?mandateAcquirer.partyId=ffaf692e-6686-11ed-9022-0242ac120002&mandateAcquirer.type=pnr&page=0&limit=100")
                .withHttpMethod(HttpMethod.GET)
                .withExpectedResponseStatus(HttpStatus.OK)
                .withExpectedResponse("expected.json")
                .sendRequestAndVerifyResponse();
    }
}
