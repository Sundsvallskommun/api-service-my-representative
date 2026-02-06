package se.sundsvall.myrepresentative.api.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MandateStatusTest {

	@Test
	void testEnumValues() {
		assertThat(MandateStatus.values()).containsExactlyInAnyOrder(
			MandateStatus.ACTIVE,
			MandateStatus.DELETED,
			MandateStatus.EXPIRED,
			MandateStatus.INACTIVE,
			MandateStatus.UNKNOWN);
	}

	@Test
	void testValueOf() {
		assertThat(MandateStatus.valueOf("ACTIVE")).isEqualTo(MandateStatus.ACTIVE);
		assertThat(MandateStatus.valueOf("DELETED")).isEqualTo(MandateStatus.DELETED);
		assertThat(MandateStatus.valueOf("EXPIRED")).isEqualTo(MandateStatus.EXPIRED);
		assertThat(MandateStatus.valueOf("INACTIVE")).isEqualTo(MandateStatus.INACTIVE);
		assertThat(MandateStatus.valueOf("UNKNOWN")).isEqualTo(MandateStatus.UNKNOWN);
	}
}
