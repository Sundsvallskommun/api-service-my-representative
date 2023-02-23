package se.sundsvall.myrepresentative.service.mandates;

import java.util.Objects;

import org.springframework.stereotype.Component;

import se.sundsvall.myrepresentative.api.model.MandateStatus;
import se.sundsvall.myrepresentative.api.model.Role;
import se.sundsvall.myrepresentative.api.model.MetaData;
import se.sundsvall.myrepresentative.api.model.mandates.Mandate;
import se.sundsvall.myrepresentative.api.model.mandates.Mandate.Permission;
import se.sundsvall.myrepresentative.api.model.ResponseAcquirer;
import se.sundsvall.myrepresentative.api.model.ResponseIssuer;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesResponse;

import generated.se.sundsvall.minaombud.Fullmaktshavare;
import generated.se.sundsvall.minaombud.HamtaBehorigheterResponse;

@Component
public class MandatesResponseMapper {

    public MandatesResponse mapFullmakterResponse(HamtaBehorigheterResponse behorigheterResponse) {
        return MandatesResponse.builder()
                .withMandates(behorigheterResponse.getKontext().stream()
                        .filter(Objects::nonNull)
                        .map(kontext -> Mandate.builder()
                                .withMandateIssuer(ResponseIssuer.builder()
                                        .withLegalId(kontext.getFullmaktsgivare().getId())
                                        .withType(kontext.getFullmaktsgivare().getTyp())
                                        .withName(kontext.getFullmaktsgivare().getNamn())
                                        .build())
                                .withMandateAcquirers(kontext.getFullmaktshavare().stream()
                                        .map(singleAquirer -> ResponseAcquirer.builder()
                                                .withLegalId(singleAquirer.getId())
                                                .withType(singleAquirer.getTyp())
                                                .withName(mapMandateAcquirerName(singleAquirer))
                                                .build())
                                        .toList())
                                .withPermissions(kontext.getBehorigheter().stream()
                                        .map(behorighet -> Permission.builder()
                                                .withCode(behorighet.getKod())
                                                .withMandateStatus(MandateStatus.fromBolagsverketValue(behorighet.getTyp()))
                                                .withMandate(behorighet.getFullmakt().toString())
                                                .build())
                                        .toList())
                                .withIssuedDate(kontext.getTidpunkt().toLocalDateTime())
                                .withMandateRole(Role.fromBolagsverketValue(kontext.getFullmaktsgivarroll().toString()))
                                .build())
                        .toList())
                .withMetaData(MetaData.builder()
                        .withPage(behorigheterResponse.getPage().getNumber())
                        .withTotalPages(behorigheterResponse.getPage().getTotalPages().intValue())
                        .withTotalRecords(behorigheterResponse.getPage().getTotalElements().intValue())
                        .withLimit(behorigheterResponse.getPage().getSize())
                        .build())
                .build();
    }

    String mapMandateAcquirerName(Fullmaktshavare fullmaktshavare) {
        String name;
        if(fullmaktshavare.getTyp().equals("orgnr")) {
            name = fullmaktshavare.getNamn();
        } else {
            name = fullmaktshavare.getFornamn() + " " + fullmaktshavare.getNamn();
        }
        return name;
    }
}
