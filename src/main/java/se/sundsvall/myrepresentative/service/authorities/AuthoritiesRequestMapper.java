package se.sundsvall.myrepresentative.service.authorities;

import static se.sundsvall.myrepresentative.service.mandates.MandatesRequestMapper.THIRD_PARTY;

import java.util.List;

import org.springframework.stereotype.Component;

import se.sundsvall.myrepresentative.api.model.GetAcquirer;
import se.sundsvall.myrepresentative.api.model.GetIssuer;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesRequest;

import generated.se.sundsvall.minaombud.HamtaFullmakterRequest;
import generated.se.sundsvall.minaombud.Identitetsbeteckning;
import generated.se.sundsvall.minaombud.PageParameters;

@Component
public class AuthoritiesRequestMapper {

	public HamtaFullmakterRequest createFullmakterRequest(AuthoritiesRequest authoritiesRequest) {
		return new HamtaFullmakterRequest()
			.fullmaktsgivare(createIdentitetsbeteckningForIssuer(authoritiesRequest.getAuthorityIssuer()))
			.fullmaktshavare(createIdentitetsbeteckningForAcquirer(authoritiesRequest.getAuthorityAcquirer()))
			.tredjeman(List.of(THIRD_PARTY))
			.page(createPageParameters(authoritiesRequest));
	}

	private Identitetsbeteckning createIdentitetsbeteckningForIssuer(GetIssuer issuer) {
		if (issuer != null) {
			return new Identitetsbeteckning(issuer.getLegalId(), issuer.getType());
		} else {
			return null;
		}
	}

	private Identitetsbeteckning createIdentitetsbeteckningForAcquirer(GetAcquirer acquirer) {
		return new Identitetsbeteckning(acquirer.getLegalId(), acquirer.getType());
	}

	private PageParameters createPageParameters(AuthoritiesRequest request) {
		return new PageParameters()
			.page(request.getPage())
			.size(request.getLimit());
	}
}
