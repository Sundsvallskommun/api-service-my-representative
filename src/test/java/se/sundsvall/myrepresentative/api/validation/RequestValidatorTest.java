package se.sundsvall.myrepresentative.api.validation;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;
import org.zalando.problem.ThrowableProblem;

import se.sundsvall.myrepresentative.TestObjectFactory;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesRequest;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesRequest;

class RequestValidatorTest {

    private RequestValidator validator = new RequestValidator();

    @Test
    void testValidateMandates_shouldNotThrowException_whenOk() {
        validator.validate(TestObjectFactory.createMandatesRequest());
    }

    @Test
    void testValidateAuthorities_shouldNotThrowException_whenOk() {
        validator.validate(TestObjectFactory.createAuthorityRequest());
    }

    @Test
    void testValidateMandatesRequest_shouldThrowException_whenMissingField() {
        MandatesRequest mandatesRequest = TestObjectFactory.createMandatesRequest();
        mandatesRequest.getMandateIssuer().setPartyId(null);

        assertThatExceptionOfType(ThrowableProblem.class).isThrownBy(() -> validator.validate(mandatesRequest))
                .withMessage("Bad Request: Both partyId and type are required when Issuer is provided");
    }

    @Test
    void testValidateAuthoritiesRequest_shouldThrowException_whenMissingField() {
        AuthoritiesRequest authorityRequest = TestObjectFactory.createAuthorityRequest();
        authorityRequest.getAuthorityIssuer().setType(null);

        assertThatExceptionOfType(ThrowableProblem.class).isThrownBy(() -> validator.validate(authorityRequest))
                .withMessage("Bad Request: Both partyId and type are required when Issuer is provided");
    }


}