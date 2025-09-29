package se.sundsvall.myrepresentative.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.myrepresentative.config.Builder;

@Builder
@Schema(description = "GrantorDetails model")
public record GrantorDetails(

	@Schema(description = "The name of the granting organization or person", example = "Ankeborgs Margarinfabrik") String name,

	@ValidUuid @Schema(description = "The partyId of the issuing organization or person", example = "fb2f0290-3820-11ed-a261-0242ac120002", requiredMode = REQUIRED) String grantorPartyId,

	@ValidUuid @Schema(description = "PartyId of the issuing person / signatory", example = "fb2f0290-3820-11ed-a261-0242ac120003", requiredMode = REQUIRED) String signatoryPartyId) {
}
