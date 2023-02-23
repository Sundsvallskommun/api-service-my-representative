package se.sundsvall.myrepresentative.api.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void fromPrivatToPrivate() {
        Role role = Role.fromBolagsverketValue("Privat");
        assertThat(role).isEqualByComparingTo(Role.PRIVATE);
    }

    @Test
    void fromOrganisationToOrganization() {
        Role role = Role.fromBolagsverketValue("Organisation");
        assertThat(role).isEqualByComparingTo(Role.ORGANIZATION);
    }

    @Test
    void fromNotSupportedToNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> Role.fromBolagsverketValue("Not supported"));
    }
}
