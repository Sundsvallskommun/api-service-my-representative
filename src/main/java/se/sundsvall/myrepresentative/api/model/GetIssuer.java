package se.sundsvall.myrepresentative.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
@Schema(description = "Base information for the issuer of mandates / authorities if provided, 'partyId' and 'type' must be set.")
@AllArgsConstructor
@NoArgsConstructor
public class GetIssuer {

	@Schema(description = "PartyId for the sole trader or organization", example = "fb2f0290-3820-11ed-a261-0242ac120002")
	private String partyId;

	@Schema(description = "Type, private person (pnr) or sole trader / organization (orgnr)", example = "pnr", allowableValues = {
		"pnr", "orgnr"
	})
	private String type;

	@JsonIgnore
	private String legalId;
}
