package se.sundsvall.myrepresentative.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import se.sundsvall.dept44.common.validators.annotation.ValidUuid;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
@Schema(description = "Base information for the acquirer of mandates / authorities")
@AllArgsConstructor
@NoArgsConstructor
public class GetAcquirer {

    @ValidUuid(message = "partyId must be present and valid")
    @Schema(description = "PartyId for the sole trader or organization", example = "fb2f0290-3820-11ed-a261-0242ac120002")
    private String partyId;

    @NotEmpty(message = "type is mandatory. 'private' or 'organization'")
    @Schema(description = "Type, private person (pnr) or sole trader / organization (orgnr)", example = "pnr", allowableValues = { "pnr", "orgnr" })
    private String type;

    @JsonIgnore
    private String legalId;
}
