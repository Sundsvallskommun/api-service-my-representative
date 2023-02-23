package se.sundsvall.myrepresentative.integration.minaombud.ombud;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import generated.se.sundsvall.minaombud.HamtaBehorigheterRequest;
import generated.se.sundsvall.minaombud.HamtaBehorigheterResponse;
import generated.se.sundsvall.minaombud.HamtaFullmakterRequest;
import generated.se.sundsvall.minaombud.HamtaFullmakterResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient(
        name = "minaombud",
        url = "${integration.minaombud.url}",
        configuration = OmbudConfiguration.class
)
@CircuitBreaker(name = "minaombud")
public interface OmbudIntegration {

    /**
     * Find all mandates for a person/organization.
     * @param request
     * @return
     */
    @PostMapping(path = "/sok/behorigheter", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    HamtaBehorigheterResponse getBehorigheter(@RequestHeader("X-Id-Token") String token, @RequestBody HamtaBehorigheterRequest request);

    /**
     * Find all authorities for a person/organization.
     * @param request
     * @return
     */
    @PostMapping(path = "/sok/fullmakter", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    HamtaFullmakterResponse getFullmakter(@RequestHeader("X-Id-Token") String token, @RequestBody HamtaFullmakterRequest request);
}
