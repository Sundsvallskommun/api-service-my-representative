package se.sundsvall.myrepresentative.service.authorities;

import org.springframework.stereotype.Component;

import se.sundsvall.myrepresentative.api.model.AuthorityStatus;
import se.sundsvall.myrepresentative.api.model.MetaData;
import se.sundsvall.myrepresentative.api.model.ResponseAcquirer;
import se.sundsvall.myrepresentative.api.model.ResponseIssuer;
import se.sundsvall.myrepresentative.api.model.Role;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesResponse;
import se.sundsvall.myrepresentative.api.model.authorities.Authority;

import generated.se.sundsvall.minaombud.FullmaktListItem;
import generated.se.sundsvall.minaombud.Fullmaktsgivare;
import generated.se.sundsvall.minaombud.Fullmaktshavare;
import generated.se.sundsvall.minaombud.HamtaFullmakterResponse;

@Component
public class AuthoritiesResponseMapper {

    public AuthoritiesResponse mapFullmakterResponse(HamtaFullmakterResponse response) {
        AuthoritiesResponse authoritiesResponse = new AuthoritiesResponse();
        response.getFullmakter()
                .forEach(item -> authoritiesResponse.addAuthority(mapAuthorityListItem(item)));

        authoritiesResponse.setMetaData(mapMetaData(response));

        return authoritiesResponse;
    }

    private MetaData mapMetaData(HamtaFullmakterResponse response) {
        return MetaData.builder()
                .withPage(response.getPage().getNumber())
                .withTotalPages(response.getPage().getTotalPages().intValue())
                .withTotalRecords(response.getPage().getTotalElements().intValue())
                .withLimit(response.getPage().getSize())
                .build();
    }

    private Authority mapAuthorityListItem(FullmaktListItem item) {

        return Authority.builder()
                .withId(item.getId().toString())
                .withStatus(AuthorityStatus.fromBolagsverketValue(item.getStatus().toString()))
                .withDescription(item.getRubrik())
                .withValidFrom(item.getGiltigFrom())
                .withValidTo(item.getGiltigTom())
                .withReferenceNumber(item.getReferensnummer())
                .withIssuedDate(item.getRegistreringstidpunkt().toLocalDateTime())
                .withAuthorityRole(Role.fromBolagsverketValue(item.getFullmaktsgivarroll().toString()))
                .withAuthorityIssuer(ResponseIssuer.builder()
                        .withLegalId(item.getFullmaktsgivare().getId())
                        .withName(mapAuthorityIssuerName(item.getFullmaktsgivare()))
                        .withType(item.getFullmaktsgivare().getTyp())
                        .build())
                .withAuthorityAcquirers(item.getFullmaktshavare().stream()
                        .map(fullmaktshavare -> ResponseAcquirer.builder()
                                .withLegalId(fullmaktshavare.getId())
                                .withName(mapAuthorityAcquirerName(fullmaktshavare))
                                .withType(fullmaktshavare.getTyp())
                                .build())
                        .toList())
                .build();
    }

    String mapAuthorityIssuerName(Fullmaktsgivare fullmaktsgivare) {
        String name;
        if(fullmaktsgivare.getTyp().equals("orgnr")) {
            name = fullmaktsgivare.getNamn();
        } else {
            name = fullmaktsgivare.getFornamn() + " " + fullmaktsgivare.getNamn();
        }
        return name;
    }

    String mapAuthorityAcquirerName(Fullmaktshavare fullmaktshavare) {
        String name;
        if(fullmaktshavare.getTyp().equals("orgnr")) {
            name = fullmaktshavare.getNamn();
        } else {
            name = fullmaktshavare.getFornamn() + " " + fullmaktshavare.getNamn();
        }
        return name;
    }
}
