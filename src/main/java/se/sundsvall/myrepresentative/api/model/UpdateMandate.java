package se.sundsvall.myrepresentative.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import se.sundsvall.myrepresentative.config.Builder;

@Builder
@Schema(description = "Mandates request model")
public record UpdateMandate(

	@Schema(description = "Mandate grantee and their details", requiredMode = NOT_REQUIRED) @NotNull @Valid GranteeDetails granteeDetails,

	@Schema(description = "The date when the mandate becomes effective", example = "2025-01-01", requiredMode = NOT_REQUIRED) @DateTimeFormat(iso = DATE) LocalDate activeFrom,

	@Schema(description = "The date after which the mandate is no longer valid", example = "2025-12-31", requiredMode = NOT_REQUIRED) @DateTimeFormat(iso = DATE) LocalDate inactiveAfter) {

}
