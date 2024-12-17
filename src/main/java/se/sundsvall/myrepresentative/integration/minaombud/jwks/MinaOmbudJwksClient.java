package se.sundsvall.myrepresentative.integration.minaombud.jwks;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static se.sundsvall.myrepresentative.integration.minaombud.jwks.MinaOmbudJwksConfiguration.CLIENT_ID;

import generated.se.sundsvall.minaombud.JwkSet;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
	name = CLIENT_ID,
	url = "${integration.minaombud.url}",
	configuration = MinaOmbudJwksConfiguration.class)
@CircuitBreaker(name = CLIENT_ID)
public interface MinaOmbudJwksClient {

	/**
	 * Fetch the jwks to be able to verify the signature from mina ombud.
	 *
	 * @param  thirdParty Sundsvalls municipality id.
	 * @return            The jwks containing the public keys
	 */
	@GetMapping(path = "/tredjeman/{tredjeman}/jwks", produces = APPLICATION_JSON_VALUE)
	JwkSet getJwks(@PathVariable("tredjeman") String thirdParty);
}
