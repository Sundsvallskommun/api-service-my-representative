package se.sundsvall.myrepresentative.integration.minaombud.jwks;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import generated.se.sundsvall.minaombud.JwkSet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
	name = "minaombudjwk",
	url = "${integration.minaombud.url}",
	configuration = MinaOmbudJwksConfiguration.class)
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
