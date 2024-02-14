package se.sundsvall.myrepresentative.service.mandates;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import se.sundsvall.dept44.test.annotation.resource.Load;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;
import se.sundsvall.myrepresentative.api.model.Role;
import se.sundsvall.myrepresentative.api.model.mandates.Mandate;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesResponse;

import generated.se.sundsvall.minaombud.HamtaBehorigheterResponse;

@ExtendWith({ ResourceLoaderExtension.class, MockitoExtension.class, SoftAssertionsExtension.class })
class MandatesResponseMapperTest {

	@InjectMocks
	private MandatesResponseMapper mandatesResponseMapper;

	@Test
	void test(@Load(value = "junit/behorigheter.json", as = Load.ResourceType.JSON) final HamtaBehorigheterResponse response, final SoftAssertions softly) {
		final MandatesResponse mandatesResponse = mandatesResponseMapper.mapFullmakterResponse(response);
		final var fullmakt = response.getKontext().getFirst().getBehorigheter().getFirst().getFullmakt();

		assertThat(mandatesResponse).isNotNull();
		final Mandate mandate = mandatesResponse.getMandates().getFirst();
		softly.assertThat(mandate.getMandateIssuer().getPartyId()).isNull();
		softly.assertThat(mandate.getMandateIssuer().getType()).isEqualTo("orgnr");
		softly.assertThat(mandate.getMandateIssuer().getName()).isEqualTo("TB Valls Golv AB");
		softly.assertThat(mandate.getMandateIssuer().getLegalId()).isEqualTo("5563454502");

		softly.assertThat(mandate.getMandateAcquirers().getFirst().getPartyId()).isNull();
		softly.assertThat(mandate.getMandateAcquirers().getFirst().getType()).isEqualTo("pnr");
		softly.assertThat(mandate.getMandateAcquirers().getFirst().getName()).isEqualTo("Karin Andersson");
		softly.assertThat(mandate.getMandateAcquirers().getFirst().getLegalId()).isEqualTo("198101032384");

		softly.assertThat(mandate.getPermissions().get(fullmakt).getFirst().getCode()).isEqualTo("d7b1de6e-8ef2-49e8-81b0-002646e3a0ff");

		softly.assertThat(mandate.getMandateRole()).isEqualByComparingTo(Role.ORGANIZATION);
		softly.assertThat(mandate.getIssuedDate()).isEqualTo(LocalDateTime.parse("2023-01-11T10:57:06.043136"));
	}
}
