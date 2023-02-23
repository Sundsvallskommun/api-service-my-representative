package se.sundsvall.myrepresentative.service.jwt;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "minaombud.jwt")
public class JwtConfigProperties {

    private String issuer;
    private String audience;
    private Duration expiration;
}
