package se.sundsvall.myrepresentative.integration.legalentity.configuration;

import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "integration.legalentity")
public record LegalEntityProperties(
	@DefaultValue("true") boolean validationEnabled,
	@DefaultValue("5") int connectTimeout,
	@DefaultValue("15") int readTimeout,
	Map<String, List<String>> whitelist) {
}
