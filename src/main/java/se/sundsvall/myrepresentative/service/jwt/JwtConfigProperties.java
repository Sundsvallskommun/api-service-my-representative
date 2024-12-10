package se.sundsvall.myrepresentative.service.jwt;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "minaombud.jwt")
public class JwtConfigProperties {

	private String issuer;
	private String audience;
	private Duration expiration;
	private Duration clockSkew;
}
