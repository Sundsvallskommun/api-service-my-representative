package se.sundsvall.myrepresentative.integration.db.entity;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEqualsFor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCodeFor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToStringExcluding;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static com.google.code.beanmatchers.BeanMatchers.registerValueGenerator;
import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SigningInformationEntityTest {

	private static final String ID = UUID.randomUUID().toString();
	private static final String ORDER_REF = UUID.randomUUID().toString();
	private static final String STATUS = "complete";
	private static final String PERSONAL_NUMBER = "200001012384";
	private static final String NAME = "John Wick";
	private static final String GIVEN_NAME = "John";
	private static final String SURNAME = "WICK";
	private static final String IP_ADDRESS = "2001:0db8:85a3:0000:0000:8a2e:0370:7334:192.168.0.1";
	private static final String UHI = "OZvYM9VvyiAmG7NA5jU5zqGcVpo=";
	private static final LocalDate BANK_ID_ISSUE_DATE = LocalDate.now();
	private static final Boolean MRTD = true;
	private static final String SIGNATURE_DATE = "<base64-encoded data>";
	private static final String OCSP_RESPONSE = "<more base64-encoded data>";
	private static final String RISK = "low";
	private static final MandateEntity MANDATE_ENTITY = new MandateEntity();
	private static final OffsetDateTime SIGNED = OffsetDateTime.now();
	private static final String[] EXCLUSIONS = List.of("signatureData", "ocspResponse").toArray(new String[0]);

	@BeforeAll
	static void beforeAll() {
		registerValueGenerator(() -> now().plusDays(new Random().nextInt()), LocalDate.class);
		registerValueGenerator(() -> OffsetDateTime.now().plusDays(new Random().nextInt()), OffsetDateTime.class);
	}

	@Test
	void testBean() {
		MatcherAssert.assertThat(SigningInformationEntity.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCodeFor("id"),
			hasValidBeanEqualsFor("id"),
			hasValidBeanToStringExcluding(EXCLUSIONS)));
	}

	@Test
	void testBuilders() {
		final var entity = new SigningInformationEntity()
			.withId(ID)
			.withOrderRef(ORDER_REF)
			.withStatus(STATUS)
			.withPersonalNumber(PERSONAL_NUMBER)
			.withName(NAME)
			.withGivenName(GIVEN_NAME)
			.withSurname(SURNAME)
			.withIpAddress(IP_ADDRESS)
			.withUhi(UHI)
			.withBankIdIssueDate(BANK_ID_ISSUE_DATE)
			.withMrtdStepUp(MRTD)
			.withSignatureData(SIGNATURE_DATE)
			.withOcspResponse(OCSP_RESPONSE)
			.withRisk(RISK)
			.withMandate(MANDATE_ENTITY)
			.withSigned(SIGNED);

		assertBean(entity);
	}

	@Test
	void testSettersAndGetters() {
		final var entity = new SigningInformationEntity();
		entity.setId(ID);
		entity.setOrderRef(ORDER_REF);
		entity.setStatus(STATUS);
		entity.setPersonalNumber(PERSONAL_NUMBER);
		entity.setName(NAME);
		entity.setGivenName(GIVEN_NAME);
		entity.setSurname(SURNAME);
		entity.setIpAddress(IP_ADDRESS);
		entity.setUhi(UHI);
		entity.setBankIdIssueDate(BANK_ID_ISSUE_DATE);
		entity.setMrtd(MRTD);
		entity.setSignatureData(SIGNATURE_DATE);
		entity.setOcspResponse(OCSP_RESPONSE);
		entity.setRisk(RISK);
		entity.setMandate(MANDATE_ENTITY);
		entity.setSigned(SIGNED);

		assertBean(entity);
	}

	private void assertBean(final SigningInformationEntity entity) {
		assertThat(entity.getId()).isEqualTo(ID);
		assertThat(entity.getOrderRef()).isEqualTo(ORDER_REF);
		assertThat(entity.getStatus()).isEqualTo(STATUS);
		assertThat(entity.getPersonalNumber()).isEqualTo(PERSONAL_NUMBER);
		assertThat(entity.getName()).isEqualTo(NAME);
		assertThat(entity.getGivenName()).isEqualTo(GIVEN_NAME);
		assertThat(entity.getSurname()).isEqualTo(SURNAME);
		assertThat(entity.getIpAddress()).isEqualTo(IP_ADDRESS);
		assertThat(entity.getUhi()).isEqualTo(UHI);
		assertThat(entity.getBankIdIssueDate()).isEqualTo(BANK_ID_ISSUE_DATE);
		assertThat(entity.getMrtd()).isEqualTo(MRTD);
		assertThat(entity.getSignatureData()).isEqualTo(SIGNATURE_DATE);
		assertThat(entity.getOcspResponse()).isEqualTo(OCSP_RESPONSE);
		assertThat(entity.getRisk()).isEqualTo(RISK);
		assertThat(entity.getMandate()).isEqualTo(MANDATE_ENTITY);
		assertThat(entity.getSigned()).isEqualTo(SIGNED);

		assertThat(entity).hasNoNullFieldsOrProperties();
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new SigningInformationEntity()).hasAllNullFieldsOrPropertiesExcept("mrtd");
	}
}
