package se.sundsvall.myrepresentative.integration.minaombud;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

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
