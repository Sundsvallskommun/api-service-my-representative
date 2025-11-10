package se.sundsvall.myrepresentative.api.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class SigningInfoTest {

	private static final String ORDER_REF = "orderRef";
	private static final String EXTERNAL_TRANSACTION_ID = "externalTransactionId";
	private static final String STATUS = "status";
	private static final String SIGNATURE = "YmFzZTY0LWVuY29kZWQgZGF0YQ==";
	private static final String OCSP_RESPONSE = "YmFzZTY0LWVuY29kZWQgZGF0YQ==";
	private static final LocalDate BANK_ID_ISSUE_DATE = LocalDate.now().minusYears(1);
	private static final String PERSONAL_NUMBER = "200001012384";
	private static final String NAME = "John Wick";
	private static final String GIVEN_NAME = "John";
	private static final String SURNAME = "Wick";
	private static final String UHI = "OZvYM9VvyiAmG7NA5jU5zqGcVpo=";
	private static final String IP_ADDRESS = "192.168.1.1";
	private static final Boolean MRTD = true;
	private static final String RISK = "low";

	@Test
	void testConstructor() {
		final var signingInfo = new SigningInfo(
			ORDER_REF,
			EXTERNAL_TRANSACTION_ID,
			STATUS,
			CompletionDataBuilder.create()
				.withSignature(SIGNATURE)
				.withOcspResponse(OCSP_RESPONSE)
				.withBankIdIssueDate(BANK_ID_ISSUE_DATE)
				.withUser(UserBuilder.create()
					.withPersonalNumber(PERSONAL_NUMBER)
					.withName(NAME)
					.withGivenName(GIVEN_NAME)
					.withSurname(SURNAME)
					.build())
				.withDevice(DeviceBuilder.create()
					.withUhi(UHI)
					.withIpAddress(IP_ADDRESS)
					.build())
				.withStepUp(StepUpBuilder.create()
					.withMrtd(MRTD)
					.build())
				.withRisk(RISK)
				.build());

		assertBean(signingInfo);
	}

	@Test
	void testBuilder() {
		final var signingInfo = SigningInfoBuilder.create()
			.withOrderRef(ORDER_REF)
			.withExternalTransactionId(EXTERNAL_TRANSACTION_ID)
			.withStatus(STATUS)
			.withCompletionData(CompletionDataBuilder.create()
				.withSignature(SIGNATURE)
				.withOcspResponse(OCSP_RESPONSE)
				.withBankIdIssueDate(BANK_ID_ISSUE_DATE)
				.withUser(UserBuilder.create()
					.withPersonalNumber(PERSONAL_NUMBER)
					.withName(NAME)
					.withGivenName(GIVEN_NAME)
					.withSurname(SURNAME)
					.build())
				.withDevice(DeviceBuilder.create()
					.withUhi(UHI)
					.withIpAddress(IP_ADDRESS)
					.build())
				.withStepUp(StepUpBuilder.create()
					.withMrtd(MRTD)
					.build())
				.withRisk(RISK)
				.build())
			.build();

		assertBean(signingInfo);
	}

	private static void assertBean(final SigningInfo signingInfo) {
		assertThat(signingInfo.orderRef()).isEqualTo(ORDER_REF);
		assertThat(signingInfo.externalTransactionId()).isEqualTo(EXTERNAL_TRANSACTION_ID);
		assertThat(signingInfo.status()).isEqualTo(STATUS);
		assertThat(signingInfo.completionData()).isNotNull();
		assertThat(signingInfo.completionData().signature()).isEqualTo(SIGNATURE);
		assertThat(signingInfo.completionData().ocspResponse()).isEqualTo(OCSP_RESPONSE);
		assertThat(signingInfo.completionData().bankIdIssueDate()).isEqualTo(BANK_ID_ISSUE_DATE);
		assertThat(signingInfo.completionData().user()).isNotNull();
		assertThat(signingInfo.completionData().user().personalNumber()).isEqualTo(PERSONAL_NUMBER);
		assertThat(signingInfo.completionData().user().name()).isEqualTo(NAME);
		assertThat(signingInfo.completionData().user().givenName()).isEqualTo(GIVEN_NAME);
		assertThat(signingInfo.completionData().user().surname()).isEqualTo(SURNAME);
		assertThat(signingInfo.completionData().device()).isNotNull();
		assertThat(signingInfo.completionData().device().uhi()).isEqualTo(UHI);
		assertThat(signingInfo.completionData().device().ipAddress()).isEqualTo(IP_ADDRESS);
		assertThat(signingInfo.completionData().stepUp()).isNotNull();
		assertThat(signingInfo.completionData().stepUp().mrtd()).isEqualTo(MRTD);
		assertThat(signingInfo.completionData().risk()).isEqualTo(RISK);

		assertThat(signingInfo).hasNoNullFieldsOrProperties();
	}
}
