package se.sundsvall.myrepresentative.integration.legalentity.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "integration.legalentity")
public record LegalEntityProperties(
	@DefaultValue("true") boolean validationEnabled,
	@DefaultValue("5") int connectTimeout,
	@DefaultValue("15") int readTimeout) {
}
