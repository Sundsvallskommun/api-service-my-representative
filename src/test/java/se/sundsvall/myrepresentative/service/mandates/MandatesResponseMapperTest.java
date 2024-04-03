package se.sundsvall.myrepresentative.service.mandates;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import se.sundsvall.dept44.test.annotation.resource.Load;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;
import se.sundsvall.myrepresentative.api.model.Role;
import se.sundsvall.myrepresentative.api.model.mandates.Mandate;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesResponse;
import se.sundsvall.myrepresentative.service.mandatetemplate.MandateTemplateService;

import generated.se.sundsvall.minaombud.HamtaBehorigheterResponse;

@ExtendWith({ResourceLoaderExtension.class, MockitoExtension.class, SoftAssertionsExtension.class})
class MandatesResponseMapperTest {

	@Mock
	private MandateTemplateService service;

	@InjectMocks
	private MandatesResponseMapper mandatesResponseMapper;

	@Test
	void test(@Load(value = "junit/behorigheter.json", as = Load.ResourceType.JSON) final HamtaBehorigheterResponse response, final SoftAssertions softly) {

		when(service.getDescriptionForTemplate(anyString())).thenReturn("someDescription");

		final MandatesResponse mandatesResponse = mandatesResponseMapper.mapFullmakterResponse(response);
		final var fullmakt = response.getKontext().getFirst().getBehorigheter().getFirst().getFullmakt();

		assertThat(mandatesResponse).isNotNull().hasNoNullFieldsOrProperties();
		final Mandate mandate = mandatesResponse.getMandates().getFirst();
		softly.assertThat(mandate.getMandateIssuer().getPartyId()).isNull();
		softly.assertThat(mandate.getMandateIssuer().getType()).isEqualTo("orgnr");
		softly.assertThat(mandate.getMandateIssuer().getName()).isEqualTo("TB Bokföringsbyrå AB");
		softly.assertThat(mandate.getMandateIssuer().getLegalId()).isEqualTo("5561929323");

		softly.assertThat(mandate.getMandateAcquirers().getFirst().getPartyId()).isNull();
		softly.assertThat(mandate.getMandateAcquirers().getFirst().getType()).isEqualTo("pnr");
		softly.assertThat(mandate.getMandateAcquirers().getFirst().getName()).isEqualTo("Beri Ylles");
		softly.assertThat(mandate.getMandateAcquirers().getFirst().getLegalId()).isEqualTo("198602262381");

		softly.assertThat(mandate.getPermissions().keySet()).hasSize(1);
		softly.assertThat(mandate.getPermissions().get(fullmakt)).hasSize(1);
		softly.assertThat(mandate.getPermissions().get(fullmakt).getFirst().getCode()).isEqualTo("573fcb75-eb8f-4226-8201-33212086544a");
		softly.assertThat(mandate.getPermissions().get(fullmakt).getFirst().getDescription()).isEqualTo("someDescription");

		softly.assertThat(mandate.getMandateRole()).isEqualByComparingTo(Role.ORGANIZATION);
		softly.assertThat(mandate.getIssuedDate()).isEqualTo(LocalDateTime.parse("2024-03-25T10:23:20.143115555"));
	}

}
