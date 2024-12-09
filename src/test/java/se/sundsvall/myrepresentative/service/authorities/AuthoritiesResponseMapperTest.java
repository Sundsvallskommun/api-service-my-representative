package se.sundsvall.myrepresentative.service.authorities;

import static org.assertj.core.api.Assertions.assertThat;

import generated.se.sundsvall.minaombud.Fullmaktsgivare;
import generated.se.sundsvall.minaombud.Fullmaktshavare;
import generated.se.sundsvall.minaombud.HamtaFullmakterResponse;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import se.sundsvall.dept44.test.annotation.resource.Load;
import se.sundsvall.dept44.test.extension.ResourceLoaderExtension;
import se.sundsvall.myrepresentative.api.model.AuthorityStatus;
import se.sundsvall.myrepresentative.api.model.Role;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesResponse;
import se.sundsvall.myrepresentative.api.model.authorities.Authority;

@ExtendWith(ResourceLoaderExtension.class)
class AuthoritiesResponseMapperTest {

	private final AuthoritiesResponseMapper mapper = new AuthoritiesResponseMapper();

	@Test
	void testMapFullmakterResponse(@Load(value = "junit/fullmakter.json", as = Load.ResourceType.JSON) HamtaFullmakterResponse fullmakterResponse) {
		final AuthoritiesResponse authoritiesResponse = mapper.mapFullmakterResponse(fullmakterResponse);

		assertThat(authoritiesResponse.getAuthorities()).hasSize(1);
		final Authority authority = authoritiesResponse.getAuthorities().get(0);
		assertThat(authority.getReferenceNumber()).isEqualTo("MOF0000000021");
		assertThat(authority.getIssuedDate()).isEqualTo(fullmakterResponse.getFullmakter().get(0).getRegistreringstidpunkt().toLocalDateTime());
		assertThat(authority.getStatus()).isEqualByComparingTo(AuthorityStatus.VALID);
		assertThat(authority.getId()).isEqualTo("95189b70-c0cc-432f-a1ef-bb75b876ab75");
		assertThat(authority.getDescription()).isEqualTo("Testfullmakt Mina ombud");
		assertThat(authority.getValidFrom()).isEqualTo(LocalDate.of(2022, 10, 24));
		assertThat(authority.getValidTo()).isEqualTo(LocalDate.of(2024, 10, 23));
		assertThat(authority.getAuthorityRole()).isEqualByComparingTo(Role.ORGANIZATION);

		assertThat(authority.getAuthorityIssuer().getLegalId()).isEqualTo("5560107053");
		assertThat(authority.getAuthorityIssuer().getType()).isEqualTo("orgnr");
		assertThat(authority.getAuthorityIssuer().getName()).isEqualTo("Testbolag 11 bokat av SKV för test och validering av långt företag namn Aktiebolag");

		assertThat(authority.getAuthorityAcquirers().get(0).getLegalId()).isEqualTo("195809132896");
		assertThat(authority.getAuthorityAcquirers().get(0).getType()).isEqualTo("pnr");
		assertThat(authority.getAuthorityAcquirers().get(0).getName()).isEqualTo("Paul Ingemar Vall");

		assertThat(authority.getAuthorityAcquirers().get(1).getLegalId()).isEqualTo("195809132897");
		assertThat(authority.getAuthorityAcquirers().get(1).getType()).isEqualTo("pnr");
		assertThat(authority.getAuthorityAcquirers().get(1).getName()).isEqualTo("Paul Inge Vallenberg");
	}

	@Test
	void testMapAuthorityIssuerName_shouldReturnOnlyNamnWhenOrgnr() {
		final Fullmaktsgivare fullmaktsgivare = new Fullmaktsgivare();
		fullmaktsgivare.setTyp("orgnr");
		fullmaktsgivare.setNamn("Name");
		fullmaktsgivare.setFornamn("FirstName");

		assertThat(mapper.mapAuthorityIssuerName(fullmaktsgivare)).isEqualTo("Name");
	}

	@Test
	void testMapAuthorityIssuerName_shouldReturnNamnAndFormamnWhenPnr() {
		final Fullmaktsgivare fullmaktsgivare = new Fullmaktsgivare();
		fullmaktsgivare.setTyp("pnr");
		fullmaktsgivare.setNamn("Name");
		fullmaktsgivare.setFornamn("FirstName");

		assertThat(mapper.mapAuthorityIssuerName(fullmaktsgivare)).isEqualTo("FirstName Name");
	}

	@Test
	void testMapAuthorityAcquirerName_shouldReturnOnlyNamnWhenOrgnr() {
		final Fullmaktshavare fullmaktshavare = new Fullmaktshavare();
		fullmaktshavare.setTyp("orgnr");
		fullmaktshavare.setNamn("Name");
		fullmaktshavare.setFornamn("FirstName");

		assertThat(mapper.mapAuthorityAcquirerName(fullmaktshavare)).isEqualTo("Name");
	}

	@Test
	void testMapAuthorityAcquirerName_shouldReturnNamnAndFormamnWhenPnr() {
		final Fullmaktshavare fullmaktshavare = new Fullmaktshavare();
		fullmaktshavare.setTyp("pnr");
		fullmaktshavare.setNamn("Name");
		fullmaktshavare.setFornamn("FirstName");

		assertThat(mapper.mapAuthorityAcquirerName(fullmaktshavare)).isEqualTo("FirstName Name");
	}
}
