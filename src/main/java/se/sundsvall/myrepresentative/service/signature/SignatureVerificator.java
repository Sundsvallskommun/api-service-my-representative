package se.sundsvall.myrepresentative.service.signature;

import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.util.Base64URL;
import generated.se.sundsvall.minaombud.JwsSig;
import org.erdtman.jcs.JsonCanonicalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;

import generated.se.sundsvall.minaombud.Behorighetskontext;
import generated.se.sundsvall.minaombud.HamtaBehorigheterResponse;

/**
 * Service for fetching JWKS from Mina Ombud and using it for verifying signatures in responses.
 * There's a lot of catching and throwing
 */
@Component
public class SignatureVerificator {

	public static final String COULD_NOT_VERIFY_RESPONSE = "Couldn't verify response from bolagsverket";

	private static final Logger LOG = LoggerFactory.getLogger(SignatureVerificator.class);
	private final ObjectMapper mapper;
	private final JwksHelper jwksHelper;

	public SignatureVerificator(JwksHelper jwksHelper) {
		this.jwksHelper = jwksHelper;

		// Create a custom objectmapper to handle the response and to keep it "intact" as we use it to verify the signature.
		this.mapper = new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
			.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
			.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	/**
	 * Takes the signature (_sig) of the response and uses it to verify the signature.
	 * 
	 * @param response The response from Mina Ombud
	 */
	public void verifySignatures(HamtaBehorigheterResponse response) {
		LOG.info("Verifying {} signature(s) from response", response.getKontext().size());

		// Loop through all the signatures in the response and verify them
		boolean allVerified = response.getKontext().stream()
			.allMatch(this::verifySignature);

		// If not all signatures are verified, the response is not verified.
		if (!allVerified) {
			LOG.warn("Not all signatures could be verified");
			throw Problem.builder()
				.withTitle(COULD_NOT_VERIFY_RESPONSE)
				.withStatus(INTERNAL_SERVER_ERROR)
				.build();
		} else {
			LOG.info("All signatures are verified");
		}
	}

	private boolean verifySignature(final Behorighetskontext kontext) {
		try {
			// Find the JWK (in the JwkSet) matching the kid in the signature, then use the JWK to verify the signature
			JWK jwkForVerifying = jwksHelper.getJWKFromProtectedHeader(kontext.getSig().getProtected());
			String kontextStringWithRemovedSig = removeSigAndCanonicalize(kontext);

			// use the public key to verify the signature
			RSASSAVerifier rsassaVerifier = new RSASSAVerifier(jwkForVerifying.toRSAKey());
			return runVerification(rsassaVerifier, kontextStringWithRemovedSig, kontext.getSig());

		} catch (Exception e) {
			// Catch everything, bottom line is we couldn't verify the signature.
			LOG.error(COULD_NOT_VERIFY_RESPONSE, e);
			return false;
		}
	}

	private String removeSigAndCanonicalize(Object object) throws IOException {
		String jsonString = mapper.writeValueAsString(object);
		JsonNode rootNode = mapper.readTree(jsonString);

		// Remove the "_sig" object from the JSON
		((ObjectNode) rootNode).remove("_sig");
		rootNode.fieldNames().forEachRemaining(fieldName -> {
			if (fieldName.equals("_sig")) {
				((ObjectNode) rootNode).remove(fieldName);
			}
		});

		String noSigJsonString = mapper.writeValueAsString(rootNode);
		return canonicalizeJsonData(noSigJsonString);
	}

	private String canonicalizeJsonData(String noSigJsonString) throws IOException {
		JsonCanonicalizer jsonCanonicalizer = new JsonCanonicalizer(noSigJsonString);
		return jsonCanonicalizer.getEncodedString();
	}

	private boolean runVerification(JWSVerifier verifier, String jsonPayload, JwsSig jwsSig) throws JOSEException, ParseException {

		var hdr = Base64URL.from(jwsSig.getProtected());
		var payload = Base64URL.encode(jsonPayload.getBytes(StandardCharsets.UTF_8));
		var signature = Base64URL.from(jwsSig.getSignature());

		var jws = new JWSObject(hdr, payload, signature);

		boolean verified = jws.verify(verifier);

		LOG.info("Signature verified? {}", verified);
		return verified;
	}
}
