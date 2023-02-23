package se.sundsvall.myrepresentative.service.signature;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import java.text.ParseException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.ThrowableProblem;

import se.sundsvall.dept44.test.annotation.resource.Load;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;
import se.sundsvall.myrepresentative.TestObjectFactory;

import generated.se.sundsvall.minaombud.HamtaBehorigheterResponse;

@ExtendWith({MockitoExtension.class, ResourceLoaderExtension.class})
class SignatureValidatorTest {

    //A header so we can test parsing ({"kid":"557b26296b7e7fbe41d0711b21c2d7bf8804507a","alg":"RS256"})
    private static final String PROTECTED_HEADER = "eyJraWQiOiI1NTdiMjYyOTZiN2U3ZmJlNDFkMDcxMWIyMWMyZDdiZjg4MDQ1MDdhIiwiYWxnIjoiUlMyNTYifQ";

    @Mock
    private JwksHelper mockJwksHelper;

    @InjectMocks
    private SignatureValidator signatureValidator;

    private JWK jwk;

    ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @BeforeEach
    void setUp(@Load(value = "junit/jwks.json", as = Load.ResourceType.STRING) String jwks) throws ParseException, JsonProcessingException {
        JWKSet jwkSet = TestObjectFactory.populateJWKSet(jwks);
        this.jwk = jwkSet.getKeys().get(0); //There's only one key in this set.
    }

    @Test
    void testvalidateSignatures(@Load(value = "junit/behorigheter.json", as = Load.ResourceType.STRING) String behorigheter) throws JsonProcessingException {
        HamtaBehorigheterResponse hamtaBehorigheterResponse = mapper.readValue(behorigheter, HamtaBehorigheterResponse.class);
        when(mockJwksHelper.getJWKFromProtectedHeader(PROTECTED_HEADER)).thenReturn(jwk);

        signatureValidator.validateSignatures(hamtaBehorigheterResponse);
    }

    @Test
    void testValidateSignature_shouldFailIfSignatureValidationFails(@Load(value = "junit/behorigheter-faulty.json", as = Load.ResourceType.STRING) String behorigheter) throws JsonProcessingException {
        //Altered response so that the calculated signature is invalid.
        HamtaBehorigheterResponse hamtaBehorigheterResponse = mapper.readValue(behorigheter, HamtaBehorigheterResponse.class);
        when(mockJwksHelper.getJWKFromProtectedHeader(PROTECTED_HEADER)).thenReturn(jwk);

        assertThatExceptionOfType(ThrowableProblem.class)
                .isThrownBy(() ->signatureValidator.validateSignatures(hamtaBehorigheterResponse))
                .withMessage("Couldn't verify response from bolagsverket");
    }
}