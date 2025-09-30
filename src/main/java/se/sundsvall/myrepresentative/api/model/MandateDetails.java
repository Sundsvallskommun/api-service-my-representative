package se.sundsvall.myrepresentative.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import se.sundsvall.myrepresentative.config.Builder;

/**
 * Mandate details.
 * 
 * @param id             the id of the mandate
 * @param grantorDetails Grantor details
 * @param granteeDetails Grantee details
 * @param municipalityId the municipalityId where the mandate was created
 * @param namespace      the namespace in which the mandate is valid
 * @param created        date and time when the mandate was created
 * @param updated        date and time when the mandate was last updated
 * @param activeFrom     date and time from when the mandate is valid
 * @param inactiveAfter  date and time when the mandate is no longer valid
 * @param status         status of the mandate (ACTIVE | INACTIVE | EXPIRED | DELETED ) backed by {@link MandateStatus}
 */
@Builder
@Schema(description = "MandateDetails model")
public record MandateDetails(

	@Schema(description = "Id of the mandate", example = "123e4567-e89b-12d3-a456-426614174000", accessMode = READ_ONLY) String id,

	@Schema(description = "Mandate grantor details", accessMode = READ_ONLY) GrantorDetails grantorDetails,

	@Schema(description = "Mandate grantee details", accessMode = READ_ONLY) GranteeDetails granteeDetails,

	@Schema(description = "MunicipalityId where the mandate was created", example = "2281", accessMode = READ_ONLY) String municipalityId,

	@Schema(description = "The namespace in which the mandate is valid", accessMode = READ_ONLY) String namespace,

	@Schema(description = "The date and time when the mandate was created", example = "2023-11-22T15:30:00", accessMode = READ_ONLY) @DateTimeFormat(iso = DATE_TIME) LocalDateTime created,

	@Schema(description = "The date and time when the mandate was changed", example = "2025-11-22T15:30:00", accessMode = READ_ONLY) @DateTimeFormat(iso = DATE_TIME) LocalDateTime updated,

	@Schema(description = "The date when the mandate becomes effective", example = "2025-01-01", accessMode = READ_ONLY) @DateTimeFormat(iso = DATE) LocalDate activeFrom,

	@Schema(description = "The date after which the mandate is no longer valid", example = "2025-12-31", accessMode = READ_ONLY) @DateTimeFormat(iso = DATE) LocalDate inactiveAfter,

	@Schema(description = "Indicates whether the mandate is active or not", example = "ACTIVE | INACTIVE | EXPIRED | DELETED", accessMode = READ_ONLY) String status) {
}
