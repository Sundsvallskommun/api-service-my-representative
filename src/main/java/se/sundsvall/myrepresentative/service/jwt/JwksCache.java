package se.sundsvall.myrepresentative.service.jwt;

import java.util.List;
import java.util.Map;

import com.github.benmanes.caffeine.cache.Cache;
import com.nimbusds.jose.jwk.JWK;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import se.sundsvall.myrepresentative.api.model.jwks.Jwks;

import lombok.Getter;

/**
 * Our own simple representation of the JSON Web Key Set (JWKS)
 * Configured as a caffeine cache for simpler eviction policies.
 */
@Getter
@Component
public final class JwksCache {

    private final Cache<String, JWK> cache;

    public JwksCache(@Qualifier("JsonWebKeyCache") Cache<String, JWK> cache) {
        this.cache = cache;
    }

    public void addJwk(JWK jwk) {
        if(jwk != null) {
            this.cache.put(jwk.getKeyID(), jwk);
        }
    }

    public Jwks getJwks() {
        List<Map<String, Object>> maps = this.cache.asMap().values()
                .stream()
                .map(JWK::toJSONObject)
                .toList();

        return new Jwks(maps);
    }
}
