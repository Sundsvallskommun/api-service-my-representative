package se.sundsvall.myrepresentative.service.signature;

import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;
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
public class SignatureValidator {

    public static final String COULD_NOT_VERIFY_RESPONSE = "Couldn't verify response from bolagsverket";

    private static final Logger LOG = LoggerFactory.getLogger(SignatureValidator.class);
    private final ObjectMapper mapper;
    private final JwksHelper jwksHelper;

    public SignatureValidator(JwksHelper jwksHelper) {
        this.jwksHelper = jwksHelper;

        //Create a custom objectmapper to handle the response and to keep it "intact" as we use it to validate the signature.
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * Takes the signature (_sig) of the response and uses it to verify the signature.
     *
     * @param response
     */
    public void validateSignatures(HamtaBehorigheterResponse response) {
        LOG.info("Verifying {} signature(s) from response", response.getKontext().size());

        //Loop through all the signatures in the response and validate them
        boolean allVerified = response.getKontext().stream()
                .allMatch(this::validateSignature);

        //If not all signatures are valid, the response is not valid.
        if (!allVerified) {
            LOG.warn("Not all signatures could be verified");
            throw Problem.builder()
                    .withTitle(COULD_NOT_VERIFY_RESPONSE)
                    .withStatus(INTERNAL_SERVER_ERROR)
                    .build();
        } else {
            LOG.info("All signatures are valid");
        }
    }

    private boolean validateSignature(final Behorighetskontext kontext) {
        try {
            //Find the JWK (in the JwkSet) matching the kid in the signature, then use the JWK to validate the signature
            JWK jwkForVerifying = jwksHelper.getJWKFromProtectedHeader(kontext.getSig().getProtected());
            String kontextStringWithRemovedSig = removeSig(kontext);

            //use the public key to verify the signature
            RSAKey publicRSAKey = jwkForVerifying.toRSAKey();
            return runValidation(publicRSAKey, kontextStringWithRemovedSig, kontext);

        } catch (Exception e) {
            //Catch everything, bottom line is we couldn't verify the signature.
            LOG.error(COULD_NOT_VERIFY_RESPONSE, e);
            return false;
        }
    }

    private String removeSig(Behorighetskontext behorighetskontext) throws IOException {
        String kontextAsJsonString = mapper.writeValueAsString(behorighetskontext);
        JsonNode rootNode = mapper.readTree(kontextAsJsonString);

        //Remove the "_sig" object from the JSON
        ((ObjectNode) rootNode).remove("_sig");
        rootNode.fieldNames().forEachRemaining(fieldName -> {
            if (fieldName.equals("_sig")) {
                ((ObjectNode) rootNode).remove(fieldName);
            }
        });

        String noSigKontext = mapper.writeValueAsString(rootNode);
        return canonicalizeJsonData(noSigKontext);
    }

    private String canonicalizeJsonData(String noSigKontext) throws IOException {
        JsonCanonicalizer jsonCanonicalizer = new JsonCanonicalizer(noSigKontext);
        return jsonCanonicalizer.getEncodedString();
    }

    private boolean runValidation(RSAKey publicRSAKey, String kontextWithRemovedSig, Behorighetskontext kontext) throws JOSEException, ParseException {
        RSASSAVerifier rsassaVerifier = new RSASSAVerifier(publicRSAKey);

        //Url-encode the payload to base64
        String kontextAsBase64 = Base64.getUrlEncoder().encodeToString(kontextWithRemovedSig.getBytes(StandardCharsets.UTF_8));

        //Construct the JWT.
        String base64 = kontext.getSig().getProtected() + "." + kontextAsBase64 + "." + kontext.getSig().getSignature();

        SignedJWT signedJWT = SignedJWT.parse(base64);

        boolean verified = signedJWT.verify(rsassaVerifier);

        LOG.info("Signature verified? {}", verified);
        return verified;
    }
}
