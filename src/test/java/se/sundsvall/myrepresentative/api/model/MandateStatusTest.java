package se.sundsvall.myrepresentative.api.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

class MandateStatusTest {

    @Test
    void testAktivToActive() {
        MandateStatus aktiv = MandateStatus.fromBolagsverketValue("Aktiv");
        assertThat(aktiv).isEqualByComparingTo(MandateStatus.ACTIVE);
    }

    @Test
    void testPassivToPassive() {
        MandateStatus passiv = MandateStatus.fromBolagsverketValue("Passiv");
        assertThat(passiv).isEqualByComparingTo(MandateStatus.PASSIVE);
    }
    @Test
    void fromNotSupportedToNull() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> MandateStatus.fromBolagsverketValue("Not supported"));
    }
}
