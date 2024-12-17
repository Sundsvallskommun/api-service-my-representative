package se.sundsvall.myrepresentative.integration.party;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "integration.party")
public class PartyProperties {

	private String url;
	private String oauth2TokenUrl;
	private String oauth2ClientId;
	private String oauth2ClientSecret;
	private Duration connectTimeout;
	private Duration readTimeout;
}
