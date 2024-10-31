package se.sundsvall.myrepresentative.api.validation;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;

import se.sundsvall.myrepresentative.api.model.GetIssuer;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesRequest;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesRequest;

@Component
public class RequestValidator {

	public void validate(MandatesRequest mandateRequest) {
		validateMandateAcquirer(mandateRequest.getMandateIssuer());
	}

	public void validate(AuthoritiesRequest authorityRequest) {
		validateMandateAcquirer(authorityRequest.getAuthorityIssuer());
	}

	/**
	 * Since issuer is not mandatory in the request, if it's provided we need to make sure all required fields are present.
	 * 
	 * @param issuer to check for required fields
	 */
	void validateMandateAcquirer(GetIssuer issuer) {
		Optional.ofNullable(issuer)
			.filter(iss -> StringUtils.isBlank(iss.getPartyId()) || StringUtils.isBlank(iss.getType()))
			.ifPresent(iss -> {
				throw Problem.valueOf(Status.BAD_REQUEST, "Both partyId and type are required when Issuer is provided");
			});
	}
}
