package se.sundsvall.myrepresentative.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of the authority")
public enum AuthorityStatus {
	ACTUAL("Actual"),
	VALID("Valid"),
	HISTORICAL("Historical");

	private final String status;

	AuthorityStatus(String status) {
		this.status = status;
	}

	/**
	 * Translates the value from bolagsverket to our enum value.
	 * 
	 * @param  status status to convert from String
	 * @return        the enum value
	 */
	public static AuthorityStatus fromBolagsverketValue(String status) {
		return switch (status.toLowerCase()) {
			case "aktuell" -> ACTUAL;
			case "giltig" -> VALID;
			case "historisk" -> HISTORICAL;
			default -> throw new IllegalArgumentException("No match found for status: " + status);
		};
	}
}
