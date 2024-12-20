package se.sundsvall.myrepresentative.service.jwt;

import static java.time.OffsetDateTime.now;
import static org.zalando.problem.Status.INTERNAL_SERVER_ERROR;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;
import lombok.Getter;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.zalando.problem.Problem;
import se.sundsvall.dept44.requestid.RequestId;
import se.sundsvall.myrepresentative.api.model.jwks.Jwks;
import se.sundsvall.myrepresentative.integration.db.JwkRepository;
import se.sundsvall.myrepresentative.integration.db.model.JwkEntity;

@Component
@Getter
public class JwtService {

	private static final Logger LOG = LoggerFactory.getLogger(JwtService.class);

	public static final JWSAlgorithm ALGORITHM = JWSAlgorithm.RS256;
	private static final String PERSONAL_NUMBER_CLAIM = "https://claims.oidc.se/1.0/personalNumber";

	private final JwtConfigProperties properties;
	private final JwkRepository jwkRepository;

	public JwtService(JwtConfigProperties properties, JwkRepository jwkRepository) throws JOSEException {
		this.properties = properties;
		this.jwkRepository = jwkRepository;
		if (!jwkRepository.existsByValidUntilAfter(now())) {
			generateAndSaveKey();
		}
	}

	/**
	 * Create a signed JWT token
	 *
	 * @param  legalId The legal id of the person we want to fetch data for
	 * @return         a signed JWT token to be used in the X-Service-Name header
	 */
	public String createSignedJwt(final String legalId) {
		// Use request-ID as "subject"" for the jwt, if the requestid for some reason is null, we cannot build the token.
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

		SignedJWT signedJWT;
		try {
			signedJWT = signJWT(getLatestKey(), claimsSet);

		} catch (JOSEException e) {
			throw Problem.builder()
				.withTitle("Error while creating request towards Mina Ombud")
				.withStatus(INTERNAL_SERVER_ERROR)
				.withDetail(e.getMessage())
				.build();
		}

		return signedJWT.serialize();
	}

	public Jwks getJwks(final String municipalityId) {
		var maps = jwkRepository.findByMunicipalityIdAndValidUntilAfter(municipalityId, now()).stream()
			.map(JwkEntity::getJwkJson)
			.map(rsaKey -> {
				try {
					return RSAKey.parse(rsaKey).toPublicJWK().toJSONObject();
				} catch (ParseException e) {
					throw Problem.builder().withTitle("Error parsing keys").withStatus(INTERNAL_SERVER_ERROR).withDetail(e.getMessage()).build();
				}
			})
			.toList();
		return new Jwks(maps);
	}

	/**
	 * Sign the JWT with our keypair
	 *
	 * @return a RSAKey
	 */
	private SignedJWT signJWT(final RSAKey rsaKey, final JWTClaimsSet claimsSet) throws JOSEException {
		// Create a new signer with the rsaKey
		JWSSigner signer = new RSASSASigner(rsaKey);

		// Sign the JWT with the signer
		JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
			.keyID(rsaKey.getKeyID())
			.build();

		SignedJWT signedJWT = new SignedJWT(header, claimsSet);

		signedJWT.sign(signer);

		return signedJWT;
	}

	void storeJWK(final RSAKey rsaKey) {
		// Set validity to double the time of JWT in case a JWT is created just before a new key is generated
		var validUntil = now()
			.plus(properties.getExpiration().multipliedBy(2))
			.plus(properties.getClockSkew());
		jwkRepository.save(JwkEntity.builder()
			.withJwkJson(rsaKey.toJSONString())
			.withValidUntil(validUntil)
			.withMunicipalityId("2281")
			.build());
	}

	private void cleanOldJWK() {
		jwkRepository.deleteByValidUntilBefore(now());
	}

	private RSAKey getLatestKey() {
		var keyString = jwkRepository.findAll(Sort.by(Sort.Direction.DESC, "validUntil")).stream()
			.findFirst()
			.map(JwkEntity::getJwkJson)
			.orElseThrow(() -> Problem.builder().withStatus(INTERNAL_SERVER_ERROR).withTitle("No RSAKey found in database").build());
		try {
			return RSAKey.parse(keyString);
		} catch (ParseException e) {
			throw Problem.builder().withStatus(INTERNAL_SERVER_ERROR).withTitle("Error parsing key").withDetail(e.getMessage()).build();
		}
	}

	/**
	 * Sets the validity of the token.
	 *
	 * @return expiration as a {@link Date}
	 */
	private Date createExpirationTimeForJwt() {
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		cal.add(Calendar.SECOND, Math.toIntExact(properties.getExpiration().get(ChronoUnit.SECONDS)));
		return new Date(cal.getTimeInMillis());
	}

	/**
	 * Generate the JWK.
	 *
	 * @return
	 */
	private RSAKey createRSAKey() throws JOSEException {
		return new RSAKeyGenerator(2048)
			.keyID(UUID.randomUUID().toString())
			.keyUse(KeyUse.SIGNATURE)
			.algorithm(ALGORITHM)
			.generate();
	}

	/**
	 * Generate a new keypair and save the JWK.
	 * This method runs on application startup if no valid key exists and
	 * according to cron expression.
	 *
	 * @throws NoSuchAlgorithmException if the algorithm is not supported
	 */
	@Transactional
	@Scheduled(cron = "${minaombud.scheduling.cron}")
	@SchedulerLock(name = "generateAndSaveKey", lockAtMostFor = "${minaombud.scheduling.lock-at-most-for}")
	public void generateAndSaveKey() throws JOSEException {
		LOG.debug("Generating new keypair and JWK");
		storeJWK(createRSAKey());
		LOG.info("Generated new keypair and JWK");
		cleanOldJWK();
	}
}
