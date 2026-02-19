package se.sundsvall.myrepresentative.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.myrepresentative.config.Builder;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Builder
@Schema(description = "GranteeDetails model")
public record GranteeDetails(

	@ValidUuid @Schema(description = "PartyId of the grantee", examples = "fb2f0290-3820-11ed-a261-0242ac120004", requiredMode = REQUIRED) String partyId) {
}
