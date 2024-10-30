package se.sundsvall.myrepresentative.api.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

class AuthorityStatusTest {

	@Test
	void fromAktuellToActual() {
		AuthorityStatus aktiv = AuthorityStatus.fromBolagsverketValue("AKTUELL");
		assertThat(aktiv).isEqualByComparingTo(AuthorityStatus.ACTUAL);
	}

	@Test
	void fromGiltigToValid() {
		AuthorityStatus giltig = AuthorityStatus.fromBolagsverketValue("GILTIG");
		assertThat(giltig).isEqualByComparingTo(AuthorityStatus.VALID);
	}

	@Test
	void fromHistoriskToHistorical() {
		AuthorityStatus historisk = AuthorityStatus.fromBolagsverketValue("HISTORISK");
		assertThat(historisk).isEqualByComparingTo(AuthorityStatus.HISTORICAL);
	}

	@Test
	void fromNullToNull() {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> AuthorityStatus.fromBolagsverketValue("Not supported"));
	}
}
