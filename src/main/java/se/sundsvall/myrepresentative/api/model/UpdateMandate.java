package se.sundsvall.myrepresentative.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.time.OffsetDateTime;
import java.util.List;
import se.sundsvall.dept44.common.validators.annotation.ValidUuid;
import se.sundsvall.myrepresentative.config.Builder;

@Builder
@Schema(description = "Mandates request model")
public record UpdateMandate(

	@ValidUuid @Schema(description = "id of the mandate", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = REQUIRED) String id,

	@Schema(description = "List of mandate grantees and their details", requiredMode = NOT_REQUIRED) List<@Valid GranteeDetails> granteeDetails,

	@Schema(description = "From when the mandate should be valid, if left empty it will be valid at time of request", example = "2025-01-01T12:00:00", requiredMode = NOT_REQUIRED) OffsetDateTime validFrom,

	@Schema(description = "To when the mandate should be valid, if left empty will be valid for 24 months", example = "2025-12-31T12:00:00", requiredMode = NOT_REQUIRED) OffsetDateTime validTo) {

}
