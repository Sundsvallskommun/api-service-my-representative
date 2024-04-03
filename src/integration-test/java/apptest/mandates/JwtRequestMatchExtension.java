package apptest.mandates;


import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.RequestMatcherExtension;
import com.google.gson.Gson;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
import wiremock.com.google.common.collect.Maps;

import java.text.ParseException;
import java.util.Map;


public class JwtRequestMatchExtension extends RequestMatcherExtension {

	private final ConsoleNotifier consoleNotifier;

	public JwtRequestMatchExtension(boolean verboseLogging) {
		consoleNotifier = new ConsoleNotifier("JwtRequestMatchExtension", verboseLogging);
	}

	@Override
	public String getName() {
		return "jwt-header-matcher";
	}


	@Override
	public MatchResult match(Request request, Parameters parameters) {

		if(!request.containsHeader(parameters.getString("header-key"))) {
			consoleNotifier.info("No match due to missing header: " + parameters.getString("header-key"));
			return MatchResult.noMatch();
		}

		String rsaJwk = new Gson().toJson(parameters.get("rsa-jwk"));
		RSAKey rsaKey = null;
		try {
			rsaKey = RSAKey.parse(rsaJwk);
			consoleNotifier.info("Successfully parsed rsa-jwk");
		} catch (ParseException e) {
			consoleNotifier.info("Warning: Could not pars 'rsa-jwk!'");
			return MatchResult.noMatch();
		}

		String jwt = request.getHeader(parameters.getString("header-key"));
		SignedJWT signedJWT = null;
		try {
			signedJWT = SignedJWT.parse(jwt);
			consoleNotifier.info("Successfully parsed jwt from request header " + parameters.getString("header-key"));
		} catch (ParseException e) {
			consoleNotifier.info(String.format("Warning: Could not pars header '%s' as signed JWT", parameters.getString("header-key")));
			return MatchResult.noMatch();
		}

		Map<String, Object> expectedHeaders = (Map<String, Object>) parameters.get("header");
		Map<String, Object> actualHeaders = signedJWT.getHeader().toJSONObject();

		var headerDiff = Maps.difference(expectedHeaders, actualHeaders);

		if(!headerDiff.areEqual()) {
			consoleNotifier.info("'header' did not match:" + headerDiff);
			return MatchResult.noMatch();
		}

		Map<String, Object> expectedPayload = (Map<String, Object>) parameters.get("payload");
		Map<String, Object> actualPayload = signedJWT.getPayload().toJSONObject();
		//Remove timestamps
		actualPayload.remove("exp");
		actualPayload.remove("iat");
		//Remove subject
		actualPayload.remove("sub");

		var payloadDiff = Maps.difference(expectedPayload, actualPayload);

		if(!payloadDiff.areEqual()) {
			consoleNotifier.info("'payload' did not match:" + payloadDiff);
			return MatchResult.noMatch();
		}

		JWSVerifier verifier = null;
		try {
			verifier = new RSASSAVerifier(rsaKey.toRSAPublicKey());
			consoleNotifier.info("Successfully extracted public key from RSAKey");
		} catch (JOSEException e) {
			consoleNotifier.info("Warning: Could not extract public key from RSAKey!");
			return MatchResult.noMatch();
		}

		try {
			var verified = signedJWT.verify(verifier);
			if(!verified) {
				consoleNotifier.info("Warning: JWT header could not be verified with public RSAKey");
			}
			return MatchResult.of(verified);
		} catch (JOSEException e) {
			consoleNotifier.info("Warning: Exception occurred when trying to verify JWT!");
			return MatchResult.noMatch();
		}
	}
}