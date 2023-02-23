package se.sundsvall.myrepresentative.service.mandates;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import se.sundsvall.myrepresentative.TestObjectFactory;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesRequest;

import generated.se.sundsvall.minaombud.HamtaBehorigheterRequest;

class MandatesRequestMapperTest {

    private final MandatesRequestMapper mapper = new MandatesRequestMapper();

    @Test
    void createBehorigheterRequest() {
        MandatesRequest mandatesRequest = TestObjectFactory.createMandatesRequest();
        HamtaBehorigheterRequest behorigheterRequest = mapper.createBehorigheterRequest(mandatesRequest);

        assertThat(behorigheterRequest.getFullmaktshavare().getId()).isEqualTo("acquirerLegalId");
        assertThat(behorigheterRequest.getFullmaktshavare().getTyp()).isEqualTo("orgnr");

        assertThat(behorigheterRequest.getFullmaktsgivare().getId()).isEqualTo("issuerLegalId");
        assertThat(behorigheterRequest.getFullmaktsgivare().getTyp()).isEqualTo("orgnr");
    }

    @Test
    void testMissingIssuer_shouldNotBeMapped() {
        MandatesRequest mandatesRequest = TestObjectFactory.createMandatesRequest();
        mandatesRequest.setMandateIssuer(null);

        HamtaBehorigheterRequest behorigheterRequest = mapper.createBehorigheterRequest(mandatesRequest);

        assertThat(behorigheterRequest.getFullmaktshavare().getId()).isEqualTo("acquirerLegalId");
        assertThat(behorigheterRequest.getFullmaktshavare().getTyp()).isEqualTo("orgnr");

        assertThat(behorigheterRequest.getFullmaktsgivare()).isNull();

    }
}