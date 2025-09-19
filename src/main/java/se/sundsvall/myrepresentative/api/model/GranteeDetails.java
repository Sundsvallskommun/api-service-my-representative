package se.sundsvall.myrepresentative.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.myrepresentative.config.Builder;

@Builder
@Schema(description = "Mandate grantee model")
public record GranteeDetails(

	@ValidUuid @Schema(description = "PartyId of the grantee", example = "fb2f0290-3820-11ed-a261-0242ac120004") String partyId) {
}
