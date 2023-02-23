package se.sundsvall.myrepresentative.config;

import java.time.Duration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.nimbusds.jose.jwk.JWK;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "minaombud.jwk")
public class JwksCacheConfig {

    //Duration of time to keep the JWK in the cache
    private Duration ttl;
    private int maximumSize;
    private int initialCapacity;

    @Bean(name = "JsonWebKeyCache")
    public Cache<String, JWK> caffeineCache() {
        return Caffeine.newBuilder()
                .initialCapacity(initialCapacity)
                .maximumSize(maximumSize)
                .expireAfterAccess(ttl)
                .recordStats()
                .build();
    }
}
