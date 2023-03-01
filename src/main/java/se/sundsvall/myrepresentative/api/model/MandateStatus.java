package se.sundsvall.myrepresentative.api.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of the mandate")
public enum MandateStatus {

    ACTIVE("Active"),
    PASSIVE("Passive");

    private final String status;

    MandateStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    /**
     * Translates the value from bolagsverket to our enum value.
     * @param status status to convert from String
     * @return the enum value
     */
    public static MandateStatus fromBolagsverketValue(String status) {
        return switch (status.toLowerCase()) {
            case "aktiv" -> ACTIVE;
            case "passiv" -> PASSIVE;
            default -> throw new IllegalArgumentException("No match found for status: " + status);
        };
    }
}
