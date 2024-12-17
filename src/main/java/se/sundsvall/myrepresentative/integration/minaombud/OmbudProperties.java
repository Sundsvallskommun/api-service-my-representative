package se.sundsvall.myrepresentative.integration.minaombud;

import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "integration.minaombud")
public class OmbudProperties {

	private String url;
	private String oauth2TokenUrl;
	private String oauth2ClientId;
	private String oauth2ClientSecret;
	private Duration connectTimeout;
	private Duration readTimeout;
	private String scope;
}
