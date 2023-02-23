package se.sundsvall.myrepresentative.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
@Schema(description = "Base response information for the acquirer of mandates / authorities")
public class ResponseAcquirer {

    @Schema(description = "PartyId for the sole trader or organization", example = "fb2f0290-3820-11ed-a261-0242ac120002")
    private String partyId;

    @Schema(description = "Type, private person (pnr) or sole trader / organization (orgnr)", example = "pnr")
    private String type;

    @Schema(description = "Name of company / person")
    private String name;

    @Schema(description = "LegalId for person, sole trader or organization")
    private String legalId;
}
