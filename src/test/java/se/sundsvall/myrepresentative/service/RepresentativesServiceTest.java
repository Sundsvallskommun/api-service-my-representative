package se.sundsvall.myrepresentative.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;
import static se.sundsvall.myrepresentative.TestObjectFactory.createMandateEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.myrepresentative.TestObjectFactory;
import se.sundsvall.myrepresentative.api.model.MandateDetailsBuilder;
import se.sundsvall.myrepresentative.api.model.MandatesBuilder;
import se.sundsvall.myrepresentative.api.model.SearchMandateParameters;
import se.sundsvall.myrepresentative.integration.db.RepositoryIntegration;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;
import se.sundsvall.myrepresentative.integration.party.PartyIntegration;

@ExtendWith(MockitoExtension.class)
class RepresentativesServiceTest {

	@Mock
	private RepositoryIntegration mockRepositoryIntegration;

	@Mock
	private ServiceMapper mockServiceMapper;

	@Mock
	private PartyIntegration mockPartyIntegration;

	@InjectMocks
	private RepresentativesService representativesService;

	@AfterEach
	void tearDown() {
		Mockito.verifyNoMoreInteractions(mockRepositoryIntegration, mockServiceMapper, mockPartyIntegration);
	}

	@Test
	void testCreateMandate() {
		final var id = UUID.randomUUID().toString();
		final var createMandate = TestObjectFactory.createMandate();
		final var mandateEntity = new MandateEntity().withId(id);
		when(mockPartyIntegration.getOrganizationLegalId(MUNICIPALITY_ID, createMandate.grantorDetails().grantorPartyId())).thenReturn(Optional.of("some-legal-id"));
		when(mockPartyIntegration.getPersonalLegalId(MUNICIPALITY_ID, createMandate.grantorDetails().signatoryPartyId())).thenReturn(Optional.of("another-legal-id"));
		when(mockRepositoryIntegration.createMandate(MUNICIPALITY_ID, NAMESPACE, createMandate)).thenReturn(mandateEntity);

		final var mandateId = representativesService.createMandate(MUNICIPALITY_ID, NAMESPACE, createMandate);

		assertThat(mandateId).isEqualTo(id);

		verify(mockPartyIntegration).getOrganizationLegalId(MUNICIPALITY_ID, createMandate.grantorDetails().grantorPartyId());
		verify(mockPartyIntegration).getPersonalLegalId(MUNICIPALITY_ID, createMandate.grantorDetails().signatoryPartyId());
		verify(mockRepositoryIntegration).createMandate(MUNICIPALITY_ID, NAMESPACE, createMandate);
	}

	@Test
	void testGetMandateDetails() {
		final var mandateId = UUID.randomUUID().toString();
		when(mockRepositoryIntegration.getMandateDetails(MUNICIPALITY_ID, NAMESPACE, mandateId)).thenReturn(Optional.of(createMandateEntity(false)));
		when(mockServiceMapper.toMandateDetailsWithSigningInfo(any(MandateEntity.class))).thenReturn(MandateDetailsBuilder.create()
			.withId(mandateId).build());

		final var mandateDetails = representativesService.getMandateDetails(MUNICIPALITY_ID, NAMESPACE, mandateId);

		assertThat(mandateDetails).isNotNull();
		assertThat(mandateDetails.id()).isEqualTo(mandateId);

		verify(mockRepositoryIntegration).getMandateDetails(MUNICIPALITY_ID, NAMESPACE, mandateId);
		verify(mockServiceMapper).toMandateDetailsWithSigningInfo(any(MandateEntity.class));
	}

	@Test
	void testGetMandate_shouldThrowNotFound_whenMandateDoesNotExist() {
		final var mandateId = UUID.randomUUID().toString();
		when(mockRepositoryIntegration.getMandateDetails(MUNICIPALITY_ID, NAMESPACE, mandateId)).thenReturn(Optional.empty());

		assertThatExceptionOfType(ThrowableProblem.class).isThrownBy(() -> representativesService.getMandateDetails(MUNICIPALITY_ID, NAMESPACE, mandateId))
			.satisfies(problem -> {
				assertThat(problem.getStatus().getStatusCode()).isEqualTo(404);
				assertThat(problem.getTitle()).isEqualTo("No mandate found");
				assertThat(problem.getMessage()).isEqualTo("No mandate found: Couldn't find any mandate with id '" + mandateId + "' for municipality '" + MUNICIPALITY_ID + "' and namespace '" + NAMESPACE + "'");
			});

		verify(mockRepositoryIntegration).getMandateDetails(MUNICIPALITY_ID, NAMESPACE, mandateId);
		verifyNoInteractions(mockServiceMapper);
	}

	@Test
	void testDeleteMandate() {
		final var mandateId = UUID.randomUUID().toString();
		doNothing().when(mockRepositoryIntegration).deleteMandate(MUNICIPALITY_ID, NAMESPACE, mandateId);

		representativesService.deleteMandate(MUNICIPALITY_ID, NAMESPACE, mandateId);

		verify(mockRepositoryIntegration).deleteMandate(MUNICIPALITY_ID, NAMESPACE, mandateId);
		verifyNoInteractions(mockServiceMapper);
	}

	@Test
	void testSearchMandates() {
		final var parameters = new SearchMandateParameters()
			.withGranteePartyId(UUID.randomUUID().toString())
			.withGrantorPartyId(UUID.randomUUID().toString())
			.withSignatoryPartyId(UUID.randomUUID().toString())
			.withPage(1)
			.withLimit(15);

		final var mandateEntity = createMandateEntity(false);
		final var page = new PageImpl<>(List.of(mandateEntity));
		final var mandates = MandatesBuilder.create().build();

		when(mockRepositoryIntegration.searchMandates(MUNICIPALITY_ID, NAMESPACE, parameters)).thenReturn(page);
		when(mockServiceMapper.toMandates(page)).thenReturn(mandates);

		final var response = representativesService.searchMandates(MUNICIPALITY_ID, NAMESPACE, parameters);
		assertThat(response).isNotNull();
		assertThat(response).isEqualTo(mandates);
		verify(mockRepositoryIntegration).searchMandates(MUNICIPALITY_ID, NAMESPACE, parameters);
		verify(mockServiceMapper).toMandates(page);
	}
}
