package se.sundsvall.myrepresentative.api.model.mandates;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import se.sundsvall.myrepresentative.api.model.ResponseAcquirer;
import se.sundsvall.myrepresentative.api.model.ResponseIssuer;
import se.sundsvall.myrepresentative.api.model.Role;

@Getter
@Setter
@Builder(setterPrefix = "with")
@Schema(description = "Mandate information model.")
public class Mandate {

	private ResponseIssuer mandateIssuer;

	@ArraySchema(schema = @Schema(implementation = ResponseAcquirer.class))
	private List<ResponseAcquirer> mandateAcquirers;

	@Schema(description = "If the issuer is an organization or private person", example = "ORGANIZATION")
	private Role mandateRole;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@Schema(description = "Date when the mandate was issued")
	private LocalDateTime issuedDate;

	@Schema(
		description = "Map of UUIDs to lists of permissions.",
		example = """
			{
				"3bfb975d-c2a9-4f16-b8e5-11c22a318fad": [
					{
						"code": "db0023d9-3d19-482f-b43c-47e0073484a2"
					}
				]
			}""")
	private Map<UUID, List<Permission>> permissions;

	@Getter
	@Setter
	@Builder(setterPrefix = "with")
	public static class Permission {

		@Schema(description = "Code for the specific permission", example = "bf1a690b-33d6-4a3e-b407-e7346fa1c97c")
		private String code;

		@Schema(description = "Description for the specific permission", example = "Fullmakt för att hantera ansökan om strandskyddsdispens")
		private String description;

	}

}
