package se.sundsvall.myrepresentative.service.jwt;

import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;

import se.sundsvall.dept44.requestid.RequestId;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;

@Component
@Getter
public class JwtService {

    private static final Logger LOG = LoggerFactory.getLogger(JwtService.class);

    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.RS256;
    private static final String PERSONAL_NUMBER_CLAIM = "https://claims.oidc.se/1.0/personalNumber";

    private final JwtConfigProperties properties;
    private final JwksCache jwksCache;

    private KeyPair keyPair;
    private String keyId;
    private RSAKey rsaKey;

    public JwtService(JwtConfigProperties properties, JwksCache jwksCache) throws NoSuchAlgorithmException {
        this.properties = properties;
        this.jwksCache = jwksCache;
        generateAndCacheKey();
    }

    /**
     * Create a signed JWT token
     * @param legalId The legal id of the person we want to fetch data for
     * @return a signed JWT token to be used in the X-Service-Name header
     * @throws JOSEException if the signing fails
     */
    public String createSignedJwt(String legalId) {
        //Use request-ID as "subject"" for the jwt, if the requestid for some reason is null, we cannot build the token.
        String requestId = Optional.ofNullable(RequestId.get())
                .orElse(UUID.randomUUID().toString());

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(requestId)
                .issuer(properties.getIssuer())
                .audience(properties.getAudience())
                .issueTime(new Date())
                .expirationTime(createExpirationTimeForJwt())
                .claim(PERSONAL_NUMBER_CLAIM, legalId)
                .build();

        SignedJWT signedJWT = null;
        try {
            signedJWT = signJWT(rsaKey, claimsSet);
        } catch (JOSEException e) {
            throw Problem.builder()
                    .withTitle("Error while creating request towards Mina Ombud")
                    .withStatus(INTERNAL_SERVER_ERROR)
                    .withDetail(e.getMessage())
                    .build();
        }

        return signedJWT.serialize();
    }

    /**
     * Sign the JWT with our keypair
     * @return a RSAKey
     */
    private SignedJWT signJWT(RSAKey rsaKey, JWTClaimsSet claimsSet) throws JOSEException {
        //Create a new signer with the rsaKey
        JWSSigner signer = new RSASSASigner(rsaKey);

        //Sign the JWT with the signer
        SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID(keyId)
                .build(), claimsSet);

        signedJWT.sign(signer);

        return signedJWT;
    }

    void cacheJWK(RSAKey rsaKey) {
        RSAKey jwk = rsaKey.toPublicJWK();
        jwksCache.addJwk(jwk);
    }

    /**
     * Create the X5tSHA256 thumbprint.
     * Haven't found a way to create the thumbprint via the JOSE-framework.
     * @return
     */
    Base64URL createX5tSha256Thumbprint() {
        byte[] sha256Bytes = DigestUtils.sha256(keyPair.getPrivate().getEncoded());
        return new Base64URL(new String(Base64.getUrlEncoder().encode(sha256Bytes)));
    }

    /**
     * Sets the validity of the token.
     * @return expiration as a {@link Date}
     */
    private Date createExpirationTimeForJwt() {
        Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        cal.add(Calendar.SECOND, Math.toIntExact(properties.getExpiration().get(ChronoUnit.SECONDS)));
        return new Date(cal.getTimeInMillis());
    }

    /**
     * Generate the JWK.
     * @return
     */
    private RSAKey createRSAKey() {
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey((RSAPrivateKey) keyPair.getPrivate())
                .algorithm(Algorithm.parse(ALGORITHM.getValue()))
                .keyID(keyId)
                .keyUse(KeyUse.SIGNATURE)
                .x509CertSHA256Thumbprint(createX5tSha256Thumbprint())
                .build();
    }

    /**
     * Generate a new keypair and cache the JWK.
     * This method runs on application startup and every time the duration has passed.
     *
     * @throws NoSuchAlgorithmException if the algorithm is not supported
     */
    @Scheduled(initialDelayString = "${minaombud.keypair.ttl}", fixedRateString = "${minaombud.keypair.ttl}")
    public void generateAndCacheKey() throws NoSuchAlgorithmException {
        LOG.debug("Generating new keypair and JWK");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM.getFamilyName());
        kpg.initialize(2048, SecureRandom.getInstanceStrong());
        this.keyPair = kpg.generateKeyPair();
        this.keyId = UUID.randomUUID().toString();
        this.rsaKey = createRSAKey();
        cacheJWK(rsaKey);
        LOG.info("Generated new keypair and JWK");
    }
}
