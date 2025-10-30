package se.sundsvall.myrepresentative.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.zalando.problem.Status.FORBIDDEN;
import static org.zalando.problem.Status.NOT_FOUND;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.createMandate;
import static se.sundsvall.myrepresentative.TestObjectFactory.createPersonEngagement;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.myrepresentative.integration.legalentity.LegalEntityIntegration;
import se.sundsvall.myrepresentative.integration.party.PartyIntegration;

@ExtendWith(MockitoExtension.class)
class LegalEntityServiceTest {

	@Mock
	private PartyIntegration mockPartyIntegration;

	@Mock
	private LegalEntityIntegration mockLegalEntityIntegration;

	@InjectMocks
	private LegalEntityService service;

	@AfterEach
	void tearDown() {
		verifyNoMoreInteractions(mockPartyIntegration, mockLegalEntityIntegration);
	}

	private static final String ORGANIZATION_LEGAL_ID = "1234567890"; // Matches the organization number in createPersonEngagement
	private static final String SIGNATORY_LEGAL_ID = "signatory-legal-id";

	@Test
	void validateSignatory() {
		final var personEngagements = List.of(createPersonEngagement());
		final var request = createMandate();
		when(mockPartyIntegration.getOrganizationLegalId(MUNICIPALITY_ID, request.grantorDetails().grantorPartyId())).thenReturn(Optional.of(ORGANIZATION_LEGAL_ID));
		when(mockPartyIntegration.getPersonalLegalId(MUNICIPALITY_ID, request.grantorDetails().signatoryPartyId())).thenReturn(Optional.of(SIGNATORY_LEGAL_ID));
		when(mockLegalEntityIntegration.getPersonEngagements(MUNICIPALITY_ID, SIGNATORY_LEGAL_ID)).thenReturn(personEngagements);

		assertDoesNotThrow(() -> service.validateSignatory(MUNICIPALITY_ID, request));

		verify(mockPartyIntegration).getOrganizationLegalId(MUNICIPALITY_ID, request.grantorDetails().grantorPartyId());
		verify(mockPartyIntegration).getPersonalLegalId(MUNICIPALITY_ID, request.grantorDetails().signatoryPartyId());
		verify(mockLegalEntityIntegration).getPersonEngagements(MUNICIPALITY_ID, SIGNATORY_LEGAL_ID);
	}

	@Test
	void validateSignatoryOrganizationNotFound() {
		final var request = createMandate();
		when(mockPartyIntegration.getOrganizationLegalId(MUNICIPALITY_ID, request.grantorDetails().grantorPartyId())).thenReturn(Optional.empty());

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> service.validateSignatory(MUNICIPALITY_ID, request))
			.satisfies(problem -> {
				assertThat(problem.getStatus()).isEqualTo(NOT_FOUND);
				assertThat(problem.getTitle()).isEqualTo("Organization not found");
				assertThat(problem.getDetail()).isEqualTo("No legalId found for partyId: " + request.grantorDetails().grantorPartyId());
			});

		verify(mockPartyIntegration).getOrganizationLegalId(MUNICIPALITY_ID, request.grantorDetails().grantorPartyId());
		verifyNoInteractions(mockLegalEntityIntegration);
	}

	@Test
	void validateSignatoryPersonNotFound() {
		final var request = createMandate();
		when(mockPartyIntegration.getOrganizationLegalId(MUNICIPALITY_ID, request.grantorDetails().grantorPartyId())).thenReturn(Optional.of(ORGANIZATION_LEGAL_ID));
		when(mockPartyIntegration.getPersonalLegalId(MUNICIPALITY_ID, request.grantorDetails().signatoryPartyId())).thenReturn(Optional.empty());

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> service.validateSignatory(MUNICIPALITY_ID, request))
			.satisfies(problem -> {
				assertThat(problem.getStatus()).isEqualTo(NOT_FOUND);
				assertThat(problem.getTitle()).isEqualTo("Person not found");
				assertThat(problem.getDetail()).isEqualTo("No legalId found for partyId: " + request.grantorDetails().signatoryPartyId());
			});

		verify(mockPartyIntegration).getPersonalLegalId(MUNICIPALITY_ID, request.grantorDetails().signatoryPartyId());
		verifyNoInteractions(mockLegalEntityIntegration);
	}

	@Test
	void validateSignatoryNotAuthorizedSignatory() {
		final var engagement = createPersonEngagement();
		engagement.setIsAuthorizedSignatory(false);

		final var personEngagements = List.of(engagement);
		final var request = createMandate();

		when(mockPartyIntegration.getOrganizationLegalId(MUNICIPALITY_ID, request.grantorDetails().grantorPartyId())).thenReturn(Optional.of(ORGANIZATION_LEGAL_ID));
		when(mockPartyIntegration.getPersonalLegalId(MUNICIPALITY_ID, request.grantorDetails().signatoryPartyId())).thenReturn(Optional.of(SIGNATORY_LEGAL_ID));
		when(mockLegalEntityIntegration.getPersonEngagements(MUNICIPALITY_ID, SIGNATORY_LEGAL_ID)).thenReturn(personEngagements);

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> service.validateSignatory(MUNICIPALITY_ID, request))
			.satisfies(problem -> {
				assertThat(problem.getStatus()).isEqualTo(FORBIDDEN);
				assertThat(problem.getTitle()).isEqualTo("Signatory not authorized");
				assertThat(problem.getDetail()).isEqualTo("The person is not an authorized signatory for the organization with legalId: " + ORGANIZATION_LEGAL_ID);
			});

		verify(mockPartyIntegration).getOrganizationLegalId(MUNICIPALITY_ID, request.grantorDetails().grantorPartyId());
		verify(mockPartyIntegration).getPersonalLegalId(MUNICIPALITY_ID, request.grantorDetails().signatoryPartyId());
		verify(mockLegalEntityIntegration).getPersonEngagements(MUNICIPALITY_ID, SIGNATORY_LEGAL_ID);
	}

	@Test
	void validateSignatoryMissingOrganizationNumber() {
		final var engagement = createPersonEngagement();
		engagement.setOrganizationNumber(null);

		final var personEngagements = List.of(engagement);
		final var request = createMandate();

		when(mockPartyIntegration.getOrganizationLegalId(MUNICIPALITY_ID, request.grantorDetails().grantorPartyId())).thenReturn(Optional.of(ORGANIZATION_LEGAL_ID));
		when(mockPartyIntegration.getPersonalLegalId(MUNICIPALITY_ID, request.grantorDetails().signatoryPartyId())).thenReturn(Optional.of(SIGNATORY_LEGAL_ID));
		when(mockLegalEntityIntegration.getPersonEngagements(MUNICIPALITY_ID, SIGNATORY_LEGAL_ID)).thenReturn(personEngagements);

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> service.validateSignatory(MUNICIPALITY_ID, request))
			.satisfies(problem -> {
				assertThat(problem.getStatus()).isEqualTo(NOT_FOUND);
				assertThat(problem.getTitle()).isEqualTo("No engagement found for organization");
				assertThat(problem.getDetail()).isEqualTo("The person does not have any engagements with the organization with legalId: " + ORGANIZATION_LEGAL_ID);
			});

		verify(mockPartyIntegration).getOrganizationLegalId(MUNICIPALITY_ID, request.grantorDetails().grantorPartyId());
		verify(mockPartyIntegration).getPersonalLegalId(MUNICIPALITY_ID, request.grantorDetails().signatoryPartyId());
		verify(mockLegalEntityIntegration).getPersonEngagements(MUNICIPALITY_ID, SIGNATORY_LEGAL_ID);
	}

	@Test
	void validateSignatoryNoMatchingOrganizationNumber() {
		final var request = createMandate();
		final var personEngagement = createPersonEngagement();
		personEngagement.setOrganizationNumber("not-found");
		final var personEngagements = List.of(personEngagement);

		when(mockPartyIntegration.getOrganizationLegalId(MUNICIPALITY_ID, request.grantorDetails().grantorPartyId())).thenReturn(Optional.of(ORGANIZATION_LEGAL_ID));
		when(mockPartyIntegration.getPersonalLegalId(MUNICIPALITY_ID, request.grantorDetails().signatoryPartyId())).thenReturn(Optional.of(SIGNATORY_LEGAL_ID));
		when(mockLegalEntityIntegration.getPersonEngagements(MUNICIPALITY_ID, SIGNATORY_LEGAL_ID)).thenReturn(personEngagements);

		assertThatExceptionOfType(ThrowableProblem.class)
			.isThrownBy(() -> service.validateSignatory(MUNICIPALITY_ID, request))
			.satisfies(problem -> {
				assertThat(problem.getStatus()).isEqualTo(NOT_FOUND);
				assertThat(problem.getTitle()).isEqualTo("No engagement found for organization");
				assertThat(problem.getDetail()).isEqualTo("The person does not have any engagements with the organization with legalId: 1234567890");
			});

		verify(mockPartyIntegration).getOrganizationLegalId(MUNICIPALITY_ID, request.grantorDetails().grantorPartyId());
		verify(mockPartyIntegration).getPersonalLegalId(MUNICIPALITY_ID, request.grantorDetails().signatoryPartyId());
		verify(mockLegalEntityIntegration).getPersonEngagements(MUNICIPALITY_ID, SIGNATORY_LEGAL_ID);
	}
}
