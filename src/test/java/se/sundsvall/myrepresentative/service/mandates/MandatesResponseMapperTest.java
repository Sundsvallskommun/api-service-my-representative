package se.sundsvall.myrepresentative.service.mandates;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
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
import se.sundsvall.myrepresentative.api.model.MandateStatus;
import se.sundsvall.myrepresentative.api.model.mandates.Mandate;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesResponse;
import se.sundsvall.myrepresentative.integration.party.PartyClient;

import generated.se.sundsvall.minaombud.HamtaBehorigheterResponse;

@ExtendWith({ResourceLoaderExtension.class, MockitoExtension.class, SoftAssertionsExtension.class})
class MandatesResponseMapperTest {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @InjectMocks
    private MandatesResponseMapper mandatesResponseMapper;

    @Test
    void test(@Load(value = "junit/behorigheter.json", as = Load.ResourceType.JSON) HamtaBehorigheterResponse response, SoftAssertions softly) {
        MandatesResponse mandatesResponse = mandatesResponseMapper.mapFullmakterResponse(response);
        assertThat(mandatesResponse).isNotNull();
        Mandate mandate = mandatesResponse.getMandates().get(0);
        softly.assertThat(mandate.getMandateIssuer().getPartyId()).isNull();
        softly.assertThat(mandate.getMandateIssuer().getType()).isEqualTo("orgnr");
        softly.assertThat(mandate.getMandateIssuer().getName()).isEqualTo("TB Valls Golv AB");
        softly.assertThat(mandate.getMandateIssuer().getLegalId()).isEqualTo("5563454502");

        softly.assertThat(mandate.getMandateAcquirers().get(0).getPartyId()).isNull();
        softly.assertThat(mandate.getMandateAcquirers().get(0).getType()).isEqualTo("pnr");
        softly.assertThat(mandate.getMandateAcquirers().get(0).getName()).isEqualTo("Karin Andersson");
        softly.assertThat(mandate.getMandateAcquirers().get(0).getLegalId()).isEqualTo("198101032384");

        softly.assertThat(mandate.getPermissions().get(0).getCode()).isEqualTo("d7b1de6e-8ef2-49e8-81b0-002646e3a0ff");
        softly.assertThat(mandate.getPermissions().get(0).getMandateStatus()).isEqualByComparingTo(MandateStatus.ACTIVE);
        softly.assertThat(mandate.getPermissions().get(0).getMandate()).isEqualTo("3bfb975d-c2a9-4f16-b8e5-11c22a318fac");

        softly.assertThat(mandate.getMandateRole()).isEqualByComparingTo(Role.ORGANIZATION);
        softly.assertThat(mandate.getIssuedDate()).isEqualTo(LocalDateTime.parse("2023-01-11T10:57:06.043136"));
    }

    static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime> {

        public JsonElement serialize(LocalDateTime date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }
    }

    static class LocalDateAdapter implements JsonSerializer<LocalDate> {

        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }
}