package se.sundsvall.myrepresentative.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import se.sundsvall.myrepresentative.config.Builder;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Builder
@Schema(description = "SigningInfo model")
public record SigningInfo(
	@Schema(description = "Reference for the signing order", examples = "131daac9-16c6-4618-beb0-365768f37288", requiredMode = REQUIRED) @NotEmpty String orderRef,

	@Schema(description = "External transactionId", examples = "87b53852-df66-4eab-bed1-873f927a2dcc", requiredMode = REQUIRED) @NotEmpty String externalTransactionId,

	@Schema(description = "Status of the signing order", examples = "complete", requiredMode = REQUIRED) @NotEmpty String status,

	@Schema(description = "Information about the user and the completed order", requiredMode = REQUIRED) @NotNull @Valid CompletionData completionData) {

	@Builder
	public record CompletionData(

		@Schema(description = "When the BankID was issued", examples = "2020-01-02", requiredMode = NOT_REQUIRED) @DateTimeFormat(iso = DATE) LocalDate bankIdIssueDate,

		@Schema(description = "The signature made by the receiving party", examples = "YmFzZTY0LWVuY29kZWQgZGF0YQ==", requiredMode = REQUIRED) @NotEmpty String signature,

		@Schema(description = "Online certificate status protocol for the signing order", examples = "YmFzZTY0LWVuY29kZWQgZGF0YQ==", requiredMode = NOT_REQUIRED) String ocspResponse,

		@Schema(description = "Indicates the risk level of the order based on data available in the order", examples = "low", requiredMode = NOT_REQUIRED) String risk,

		@Schema(description = "Information regarding the signing party", requiredMode = REQUIRED) @NotNull @Valid User user,

		@Schema(description = "Information regarding the device used for the signing order", requiredMode = REQUIRED) @NotNull @Valid Device device,

		@Schema(description = "Information about possible additional verifications that were part of the signing order", requiredMode = NOT_REQUIRED) StepUp stepUp) {

		@Builder
		public record User(
			@Schema(description = "Personal identity number for the signing party", examples = "200001012384", requiredMode = REQUIRED) @NotEmpty String personalNumber,

			@Schema(description = "Full name of the signing party", examples = "John Wick", requiredMode = NOT_REQUIRED) String name,

			@Schema(description = "First name of the signing party", examples = "John", requiredMode = REQUIRED) @NotEmpty String givenName,

			@Schema(description = "Last name of the signing party", examples = "Wick", requiredMode = REQUIRED) @NotEmpty String surname) {
		}

		@Builder
		public record StepUp(
			@Schema(description = "Whether an MRTD check was performed before the order was completed", examples = "true", requiredMode = NOT_REQUIRED) Boolean mrtd) {
		}

		@Builder
		public record Device(
			@Schema(description = "Ip address used when the letter was signed", examples = "192.168.1.1", requiredMode = REQUIRED) @NotEmpty String ipAddress,

			@Schema(description = "The Unique Hardware Identifier for the user’s device holding the BankID", examples = "OZvYM9VvyiAmG7NA5jU5zqGcVpo=", requiredMode = NOT_REQUIRED) String uhi) {
		}
	}
}
