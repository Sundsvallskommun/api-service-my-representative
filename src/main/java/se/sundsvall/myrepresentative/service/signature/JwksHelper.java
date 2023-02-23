package se.sundsvall.myrepresentative.service.signature;

import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;
import static se.sundsvall.myrepresentative.service.signature.SignatureValidator.COULD_NOT_VERIFY_RESPONSE;

import java.util.Base64;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.webjars.NotFoundException;
import org.zalando.problem.Problem;

import se.sundsvall.myrepresentative.integration.minaombud.jwks.MinaOmbudJwksClient;

/**
 * Handling of JWKS from Mina Ombud and finding the correct key to use for verifying signatures.
 */
@Component
public class JwksHelper {

    private static final Logger LOG = LoggerFactory.getLogger(JwksHelper.class);

    private static final String THIRD_PARTY= "2120002411";

    private JWKSet jwkSet;

    private final MinaOmbudJwksClient jwksClient;

    public JwksHelper(MinaOmbudJwksClient jwksClient) {
        this.jwksClient = jwksClient;
    }

    public JWK getJWKFromProtectedHeader(String protectedHeader) throws NotFoundException, JsonProcessingException {
        String kidToFind = getKidFromProtectedHeader(protectedHeader);
        LOG.info("Looking for kid in jwks: {}", kidToFind);

        populateJwksIfMissing();

        //Check for a cached Jwk, if we don't have it (or couldn't find it after a second fetch) it will throw an exception
        validateKidIsCached(kidToFind);

        return jwkSet.getKeys().stream()
                .filter(Objects::nonNull)
                .filter(jwk -> StringUtils.isNotBlank(jwk.getKeyID()) && jwk.getKeyID().equals(kidToFind))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Couldn't find JWK with kid: " + kidToFind));
    }

    /**
     * Check if we have a saved kid in the JWK-set, if not fetch it from Mina Ombud.
     * If we still cannot find the kid, throw an exception.
     * @param kidToFind from the "Sig"-object from Mina Ombud
     */
    void validateKidIsCached(String kidToFind) {
        //If the kid is missing, update the JWK-set and check again.
        if(isKidMissingInKeySet(kidToFind)) {
            updateJwkSet();

            //If we still cannot find it throw an exception, should never happen unless Mina Ombud isn't working as intended.
            if(isKidMissingInKeySet(kidToFind)) {
                LOG.error("Could not find kid in JWK-set");
                throw Problem.builder()
                        .withTitle(COULD_NOT_VERIFY_RESPONSE)
                        .withStatus(INTERNAL_SERVER_ERROR)
                        .build();
            }
        }
    }

    /**
     * Check if there's no match between the kid (from the response) and the kid in the JWK-set.
     * @param kidToFind
     * @return true if no match, false if it exists.
     */
    boolean isKidMissingInKeySet(String kidToFind) {
        //Check if no kid in the jwks is matching the one we want.
        return jwkSet.getKeys().stream()
                .noneMatch(jwk -> jwk.getKeyID().equals(kidToFind));
    }

    /**
     * Extracts the "kid" parameter value from a "protected header"
     * @param protectedHeader from the "Sig"-object from Mina Ombud
     * @return the kid value as a String
     * @throws JsonProcessingException if the BASE64-urldecoded protectedHeader is not a valid JSON
     */
    String getKidFromProtectedHeader(String protectedHeader) throws JsonProcessingException {
        //The protected parameter contains the kid to find the correct JWK to use
        //Base64-decode the protected parameter to get the JSON, which will contain the kid.
        String decodedProtected = new String(Base64.getUrlDecoder().decode(protectedHeader));

        //Parse to a JSON object
        JsonNode jsonNode = new ObjectMapper().readTree(decodedProtected);

        //Get the kid parameter value from the JSON object
        return jsonNode.get("kid").asText();
    }

    /**
     * Populate the jwks, check for null since we don't want to do it each time.
     * Also cannot set this in the constructor since (when testing) wiremock isn't running before the test is initiated.
     */
    void populateJwksIfMissing() {
        if(this.jwkSet == null) {
            updateJwkSet();
        }
    }

    /**
     * "Force"-fetch an update of the jwk-set
     */
    private void updateJwkSet() {
        LOG.info("Updating JWK set from Mina Ombud");
        jwkSet = jwksClient.getJwks(THIRD_PARTY);
    }
}
