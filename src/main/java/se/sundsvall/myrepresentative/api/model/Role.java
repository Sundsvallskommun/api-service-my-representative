package se.sundsvall.myrepresentative.api.model;

public enum Role {

    PRIVATE("PRIVATE"),
    ORGANIZATION("ORGANIZATION");

    private final String aRole;

    Role(String aRole) {
        this.aRole = aRole;
    }

    public String getRole() {
        return aRole;
    }

    /**
     * Translates the value from bolagsverket to our enum value.
     * @param role
     * @return null if no match is found
     */
    public static Role fromBolagsverketValue(String role) {
        return switch (role.toLowerCase()) {
            case "privat" -> PRIVATE;
            case "organisation" -> ORGANIZATION;
            default -> throw new IllegalArgumentException("No match found for role: " + role);
        };
    }
}
