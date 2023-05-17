package se.sundsvall.myrepresentative.service.mandates;

import org.springframework.stereotype.Component;

import se.sundsvall.myrepresentative.api.model.GetAcquirer;
import se.sundsvall.myrepresentative.api.model.GetIssuer;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesRequest;

import generated.se.sundsvall.minaombud.HamtaBehorigheterRequest;
import generated.se.sundsvall.minaombud.Identitetsbeteckning;
import generated.se.sundsvall.minaombud.PageParameters;

@Component
public class MandatesRequestMapper {

    //Always Sundsvall municipalitys organization number, since we're the ones mediating the service (third party).
    public static final String THIRD_PARTY = "2120002411";

    public HamtaBehorigheterRequest createBehorigheterRequest(MandatesRequest mandatesRequest) {
        HamtaBehorigheterRequest hamtaBehorigheterRequest = new HamtaBehorigheterRequest(createIdentitetsbeteckningForAcquirer(mandatesRequest.getMandateAcquirer()), THIRD_PARTY);
        return hamtaBehorigheterRequest
                .fullmaktsgivare(createIdentitetsbeteckningForIssuer(mandatesRequest.getMandateIssuer()))
                .behorigheter(mandatesRequest.getMandates())
                .page(createPageParameters(mandatesRequest));
    }

    private Identitetsbeteckning createIdentitetsbeteckningForIssuer(GetIssuer issuer) {
        if(issuer != null) {
            return new Identitetsbeteckning(issuer.getLegalId(), issuer.getType());
        } else {
            return null;
        }
    }

    private Identitetsbeteckning createIdentitetsbeteckningForAcquirer(GetAcquirer issuer) {
        if(issuer != null) {
            return new Identitetsbeteckning(issuer.getLegalId(), issuer.getType());
        } else {
            return null;
        }
    }

    private PageParameters createPageParameters(MandatesRequest request) {
        return new PageParameters()
                .page(request.getPage())
                .size(request.getLimit());
    }
}
