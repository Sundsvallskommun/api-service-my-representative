package se.sundsvall.myrepresentative.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import se.sundsvall.myrepresentative.config.Builder;

/**
 * Mandate details.
 * 
 * @param grantorDetails Grantor details
 * @param granteeDetails list of grantees and their details
 * @param created        date and time when the mandate was created
 * @param validFrom      date and time from when the mandate is valid
 * @param validTo        date and time when the mandate is no longer valid
 * @param status         status of the mandate (ACTIVE | INACTIVE | DELETED) backed by {@link MandateStatus}
 */
@Builder
@Schema(description = "MandateDetails model")
public record MandateDetails(

	@Schema(description = "Id of the mandate", example = "123e4567-e89b-12d3-a456-426614174000", accessMode = READ_ONLY) String id,

	@Schema(description = "Mandate grantor details", accessMode = READ_ONLY) GrantorDetails grantorDetails,

	@ArraySchema(schema = @Schema(implementation = GranteeDetails.class, accessMode = READ_ONLY)) List<GranteeDetails> granteeDetails,

	@Schema(description = "Date and time when the mandate was created", example = "2023-11-22T15:30:00+03:00", accessMode = READ_ONLY) @DateTimeFormat(iso = DATE_TIME) OffsetDateTime created,

	@Schema(description = "Date and time from when the mandate should be valid, if left empty it will be set to time of creation", example = "2025-01-01T12:00:00", accessMode = READ_ONLY) @DateTimeFormat(iso = DATE_TIME) OffsetDateTime validFrom,

	@Schema(description = "Date and time when the mandate should no longer be valid, if left empty it will be valid for 24 months", example = "2025-12-31T12:00:00", accessMode = READ_ONLY) @DateTimeFormat(iso = DATE_TIME) OffsetDateTime validTo,

	@Schema(description = "Indicates whether the mandate is active or not", example = "ACTIVE | INACTIVE | DELETED", accessMode = READ_ONLY) String status) {
}
