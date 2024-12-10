package se.sundsvall.myrepresentative.service.mandates;

import generated.se.sundsvall.minaombud.Fullmaktshavare;
import generated.se.sundsvall.minaombud.HamtaBehorigheterResponse;
import generated.se.sundsvall.minaombud.UtdeladBehorighet;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import se.sundsvall.myrepresentative.api.model.MetaData;
import se.sundsvall.myrepresentative.api.model.ResponseAcquirer;
import se.sundsvall.myrepresentative.api.model.ResponseIssuer;
import se.sundsvall.myrepresentative.api.model.Role;
import se.sundsvall.myrepresentative.api.model.mandates.Mandate;
import se.sundsvall.myrepresentative.api.model.mandates.Mandate.Permission;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesResponse;
import se.sundsvall.myrepresentative.service.mandatetemplate.MandateTemplateService;

@Component
public class MandatesResponseMapper {

	private final MandateTemplateService service;

	public MandatesResponseMapper(final MandateTemplateService service) {
		this.service = service;
	}

	public MandatesResponse mapFullmakterResponse(final String municipalityId, final HamtaBehorigheterResponse behorigheterResponse) {
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
						.map(singleAcquirer -> ResponseAcquirer.builder()
							.withLegalId(singleAcquirer.getId())
							.withType(singleAcquirer.getTyp())
							.withName(mapMandateAcquirerName(singleAcquirer))
							.build())
						.toList())
					.withPermissions(kontext.getBehorigheter().stream()
						.collect(Collectors.groupingBy(
							UtdeladBehorighet::getFullmakt,
							Collectors.mapping(
								behorighet -> Permission.builder()
									.withCode(behorighet.getKod())
									.withDescription(service.getDescriptionForTemplate(municipalityId, behorighet.getKod()))
									.build(),
								Collectors.toList()))))
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

	String mapMandateAcquirerName(final Fullmaktshavare fullmaktshavare) {
		if (fullmaktshavare.getTyp().equals("orgnr")) {
			return fullmaktshavare.getNamn();
		}
		return "%s %s".formatted(fullmaktshavare.getFornamn(), fullmaktshavare.getNamn());
	}

}
