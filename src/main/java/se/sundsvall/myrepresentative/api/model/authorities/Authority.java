package se.sundsvall.myrepresentative.api.model.authorities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import se.sundsvall.myrepresentative.api.model.ResponseAcquirer;
import se.sundsvall.myrepresentative.api.model.AuthorityStatus;
import se.sundsvall.myrepresentative.api.model.ResponseIssuer;
import se.sundsvall.myrepresentative.api.model.Role;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
@Schema(description = "Authority information model.")
public class Authority {

	private ResponseIssuer authorityIssuer;

	@ArraySchema(schema = @Schema(implementation = ResponseAcquirer.class))
	private List<ResponseAcquirer> authorityAcquirers;

	@Schema(description = "If the issuer is an organization or private person", example = "ORGANIZATION")
	private Role authorityRole;

	@Schema(description = "Reference number intended as a reference between client and third party", example = "MOF1234567890")
	private String referenceNumber;

	@Schema(description = "Status of the authority", example = "ACTIVE")
	private AuthorityStatus status;

	@Schema(description = "Unique ID for the authority", example = "bf31188a-bfbb-4f23-a60a-89c75d009b53")
	private String id;

	@Schema(description = "What the authority represents")
	private String description;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Schema(description = "Date when the authority was issued")
	private LocalDateTime issuedDate;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Schema(description = "Date from when the authority is valid")
	private LocalDate validFrom;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Schema(description = "Date to when the authority ceased to be valid")
	private LocalDate validTo;
}
