package se.sundsvall.myrepresentative.api.model;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import org.jetbrains.annotations.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import se.sundsvall.myrepresentative.config.Builder;

@Builder
@Schema(description = "SigningInfo model")
public record SigningInfo(
	@Schema(description = "Status of the signing order", example = "complete", requiredMode = REQUIRED) @NotEmpty String status,

	@Schema(description = "Timestamp when the letter was signed by receiving party", example = "2025-10-01T15:30:00+02:00", requiredMode = REQUIRED) @NotNull @DateTimeFormat(iso = DATE_TIME) OffsetDateTime signed,

	@Schema(description = "Reference for the signing order", example = "131daac9-16c6-4618-beb0-365768f37288", requiredMode = REQUIRED) @NotEmpty String orderRef,

	@Schema(description = "The signature made by the receiving party", example = "YmFzZTY0LWVuY29kZWQgZGF0YQ==", requiredMode = REQUIRED) @NotEmpty String signature,

	@Schema(description = "Online certificate status protocol for the signing order", example = "YmFzZTY0LWVuY29kZWQgZGF0YQ==", requiredMode = REQUIRED) @NotEmpty String ocspResponse,

	@Schema(description = "Information regarding the signing party", requiredMode = REQUIRED) @NotNull @Valid User user,

	@Schema(description = "Information regarding the device used for the signing order", requiredMode = REQUIRED) @NotNull @Valid Device device,

	@Schema(description = "Information about possible additional verifications that were part of the signing order", requiredMode = REQUIRED) @NotNull @Valid StepUp stepUp,

	@Schema(description = "When the BankID was issued", example = "2020-01-02", requiredMode = REQUIRED) @NotNull @DateTimeFormat(iso = DATE) LocalDate issued,

	@Schema(description = "Indicates the risk level of the order based on data available in the order", example = "low", requiredMode = REQUIRED) @NotEmpty String risk) {

	@Builder
	public record User(
		@Schema(description = "Personal identity number for the signing party", example = "200001012384", requiredMode = REQUIRED) @NotEmpty String personalNumber,

		@Schema(description = "Full name of the signing party", example = "John Wick", requiredMode = REQUIRED) @NotEmpty String name,

		@Schema(description = "First name of the signing party", example = "John", requiredMode = REQUIRED) @NotEmpty String givenName,

		@Schema(description = "Last name of the signing party", example = "Wick", requiredMode = REQUIRED) @NotEmpty String surname) {
	}

	@Builder
	public record StepUp(
		@Schema(description = "Whether an MRTD check was performed before the order was completed", example = "true", requiredMode = REQUIRED) boolean mrtd) {
	}

	@Builder
	public record Device(
		@Schema(description = "Ip address used when the letter was signed", example = "192.168.1.1", requiredMode = REQUIRED) @NotEmpty String ipAddress,

		@Schema(description = "The Unique Hardware Identifier for the user’s device holding the BankID", example = "OZvYM9VvyiAmG7NA5jU5zqGcVpo=", requiredMode = REQUIRED) @NotEmpty String uhi) {
	}
}
