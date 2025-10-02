package se.sundsvall.myrepresentative.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.jetbrains.annotations.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import se.sundsvall.myrepresentative.api.validation.ValidDateRange;
import se.sundsvall.myrepresentative.config.Builder;

@Builder
@Schema(description = "CreateMandate model")
@ValidDateRange
public record CreateMandate(

	@Schema(description = "Mandate grantor information", requiredMode = REQUIRED) @NotNull @Valid GrantorDetails grantorDetails,

	@Schema(description = "List of grantees", requiredMode = REQUIRED) @NotNull @Valid GranteeDetails granteeDetails,

	@Schema(description = "The date when the mandate becomes effective", example = "2025-08-01", requiredMode = REQUIRED) @NotNull @DateTimeFormat(iso = DATE) LocalDate activeFrom,

	@Schema(description = "The date after which the mandate is no longer valid", example = "2025-12-31", requiredMode = REQUIRED) @NotNull @DateTimeFormat(iso = DATE) LocalDate inactiveAfter) {
}
