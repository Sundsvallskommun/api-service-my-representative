package se.sundsvall.myrepresentative;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;

import se.sundsvall.myrepresentative.api.model.AuthorityStatus;
import se.sundsvall.myrepresentative.api.model.GetAcquirer;
import se.sundsvall.myrepresentative.api.model.GetIssuer;
import se.sundsvall.myrepresentative.api.model.ResponseAcquirer;
import se.sundsvall.myrepresentative.api.model.ResponseIssuer;
import se.sundsvall.myrepresentative.api.model.Role;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesRequest;
import se.sundsvall.myrepresentative.api.model.authorities.AuthoritiesResponse;
import se.sundsvall.myrepresentative.api.model.authorities.Authority;
import se.sundsvall.myrepresentative.api.model.mandates.Mandate;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesRequest;
import se.sundsvall.myrepresentative.api.model.mandates.MandatesResponse;

import generated.se.sundsvall.minaombud.Jwk;
import generated.se.sundsvall.minaombud.JwkSet;

public class TestObjectFactory {

    public static ObjectMapper createObjectMapperWithOffsetDateTimeSupport() {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(OffsetDateTime.class, new JsonSerializer<OffsetDateTime>() {
            @Override
            public void serialize(OffsetDateTime offsetDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
                jsonGenerator.writeString(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(offsetDateTime));
            }
        });
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

    public static MandatesRequest createMandatesRequest() {
        MandatesRequest request = MandatesRequest.builder()
                .withMandateIssuer(GetIssuer.builder()
                        .withPartyId("issuerPartyId")
                        .withType("orgnr")
                        .withLegalId("issuerLegalId")
                        .build())
                .withMandateAcquirer(GetAcquirer.builder()
                        .withPartyId("acquirerPartyId")
                        .withType("orgnr")
                        .withLegalId("acquirerLegalId")
                        .build())
                .withMandates(List.of("mandate1", "mandate2"))
                .build();

        request.setPage(1);
        request.setLimit(2);

        return request;
    }

    public static MandatesResponse createMandatesResponse() {
        return MandatesResponse.builder()
                .withMandates(List.of(Mandate.builder()
                        .withMandateRole(Role.ORGANIZATION)
                        .withIssuedDate(LocalDateTime.now())
                        .withPermissions(List.of(Mandate.Permission.builder()
                                .withMandate("mandate1")
                                .withCode("code1")
                                //.withMandateStatus(MandateStatus.ACTIVE)
                                .build(), Mandate.Permission.builder()
                                .withMandate("mandate2")
                                .withCode("code2")
                                //.withMandateStatus(MandateStatus.ACTIVE)
                                .build()))
                        .withMandateIssuer(ResponseIssuer.builder()
                                .withPartyId("issuerPartyId")
                                .withName("Issuer name")
                                .withType("orgnr")
                                .withLegalId("0987654321")
                                .build())
                        .withMandateAcquirers(List.of(ResponseAcquirer.builder()
                                .withPartyId("acquirerPartyId")
                                .withName("Acquirer name")
                                .withType("orgnr")
                                .withLegalId("1234567890")
                                .build()))
                        .build()))
                .build();
    }

    public static AuthoritiesRequest createAuthorityRequest() {
        return AuthoritiesRequest.builder()
                .withAuthorityIssuer(GetIssuer.builder()
                        .withPartyId("issuerPartyId")
                        .withType("orgnr")
                        .withLegalId("issuerLegalId")
                        .build())
                .withAuthorityAcquirer(GetAcquirer.builder()
                        .withPartyId("acquirerPartyId")
                        .withType("orgnr")
                        .withLegalId("acquirerLegalId")
                        .build())
                .build();
    }

    public static AuthoritiesResponse createAuthorityResponse() {

        LocalDateTime localDateTime = LocalDateTime.parse("2022-10-24T13:48:13.705856+02:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        return AuthoritiesResponse.builder()
                .withAuthorities(List.of(Authority.builder()
                                .withAuthorityIssuer(ResponseIssuer.builder()
                                        .withPartyId("issuerPartyId")
                                        .withName("Issuer name")
                                        .withType("orgnr")
                                        .withLegalId("0987654321")
                                        .build())
                                .withAuthorityAcquirers(List.of(ResponseAcquirer.builder()
                                        .withPartyId("acquirerPartyId")
                                        .withName("Acquirer name")
                                        .withType("orgnr")
                                        .withLegalId("1234567890")
                                        .build()))
                                .withAuthorityRole(Role.ORGANIZATION)
                                .withDescription("Testfullmakt Mina ombud")
                                .withIssuedDate(localDateTime)
                                .withId("95189b70-c0cc-432f-a1ef-bb75b876ab75")
                                .withReferenceNumber("MOF0000000021")
                                .withStatus(AuthorityStatus.VALID)
                                .withValidFrom(LocalDate.of(2022, 10, 24))
                                .withValidTo(LocalDate.of(2024, 10, 24))
                        .build()))
                .build();
    }

    public static JWKSet populateJWKSet(String jwks) throws JsonProcessingException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        JwkSet jwkSet = mapper.readValue(jwks, JwkSet.class);

        List<JWK> jwkList = new ArrayList<>();  //Temp list used to initialize the JWKSet at the end

        for (Jwk jwk : jwkSet.getKeys()) {
            //Parse the JWK to a JOSE JWK
            JWK parsedJwk = JWK.parse(jwk);
            jwkList.add(parsedJwk);
        }

        return new JWKSet(jwkList);
    }
}
