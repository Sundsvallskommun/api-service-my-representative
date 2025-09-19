package se.sundsvall.myrepresentative.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.time.OffsetDateTime;
import java.util.List;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.format.annotation.DateTimeFormat;
import se.sundsvall.myrepresentative.config.Builder;

@Builder
@Schema(description = "CreateMandate model")
public record CreateMandate(

	@Schema(description = "Mandate grantor information", requiredMode = REQUIRED) GrantorDetails grantorDetails,

	@NotEmpty @UniqueElements @Schema(description = "List of grantees", requiredMode = REQUIRED) List<@Valid GranteeDetails> granteeDetails,

	@Schema(description = "From when the mandate should be valid, if left empty will be valid at time of request", example = "2025-01-01T12:00:00+01:00", requiredMode = NOT_REQUIRED) @DateTimeFormat(iso = DATE_TIME) OffsetDateTime validFrom,

	@Schema(description = "To when the mandate should be valid, if left empty will be valid for 24 months", example = "2025-12-31T12:00:00", requiredMode = NOT_REQUIRED) @DateTimeFormat(iso = DATE_TIME) OffsetDateTime validTo) {
}
