package se.sundsvall.myrepresentative.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
import se.sundsvall.myrepresentative.api.model.CreateMandateBuilder;
import se.sundsvall.myrepresentative.api.model.MandateDetailsBuilder;
import se.sundsvall.myrepresentative.integration.db.MandateRepository;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;

@ExtendWith(MockitoExtension.class)
class RepresentativesServiceTest {

	@Mock
	private MandateRepository mockMandateRepository;

	@Mock
	private Mapper mockMapper;

	@InjectMocks
	private RepresentativesService representativesService;

	@AfterEach
	void tearDown() {
		Mockito.verifyNoMoreInteractions(mockMandateRepository, mockMapper);
	}

	@Test
	void testCreateMandate() {
		final var id = UUID.randomUUID().toString();
		final var createMandate = new CreateMandateBuilder().create().build();
		final var mandateEntity = new MandateEntity().withId(id);
		when(mockMapper.toMandateEntity(anyString(), anyString(), any(CreateMandate.class))).thenReturn(mandateEntity);
		when(mockMandateRepository.save(any(MandateEntity.class))).thenReturn(mandateEntity);

		final var mandateId = representativesService.createMandate(MUNICIPALITY_ID, NAMESPACE, createMandate);

		assertThat(mandateId).isEqualTo(id);

		verify(mockMapper).toMandateEntity(MUNICIPALITY_ID, NAMESPACE, createMandate);
		verify(mockMandateRepository).save(mandateEntity);
	}

	@Test
	void testGetMandateDetails() {
		final var mandateId = UUID.randomUUID().toString();
		final var mandateEntity = new MandateEntity().withId(mandateId);
		final var mandateDetails = MandateDetailsBuilder.create().build();
		when(mockMandateRepository.findByIdAndMunicipalityIdAndNamespace(anyString(), anyString(), anyString())).thenReturn(java.util.Optional.of(mandateEntity));
		when(mockMapper.toMandateDetails(any(MandateEntity.class))).thenReturn(mandateDetails);

		final var result = representativesService.getMandateDetails(MUNICIPALITY_ID, NAMESPACE, mandateId);

		assertThat(result).isEqualTo(mandateDetails);
		verify(mockMandateRepository).findByIdAndMunicipalityIdAndNamespace(mandateId, MUNICIPALITY_ID, NAMESPACE);
		verify(mockMapper).toMandateDetails(mandateEntity);
	}

	@Test
	void testGetMandate_shouldThrowNotFound_whenMandateDoesNotExist() {
		final var mandateId = UUID.randomUUID().toString();
		when(mockMandateRepository.findByIdAndMunicipalityIdAndNamespace(anyString(), anyString(), anyString())).thenReturn(Optional.empty());

		assertThatExceptionOfType(ThrowableProblem.class).isThrownBy(() -> representativesService.getMandateDetails(MUNICIPALITY_ID, NAMESPACE, mandateId))
			.satisfies(problem -> {
				assertThat(problem.getStatus().getStatusCode()).isEqualTo(404);
				assertThat(problem.getTitle()).isEqualTo("No mandate found");
				assertThat(problem.getMessage()).isEqualTo("No mandate found: Couldn't find any mandate with id '" + mandateId + "' for municipality '" + MUNICIPALITY_ID + "' and namespace '" + NAMESPACE + "'");
			});

		verify(mockMandateRepository).findByIdAndMunicipalityIdAndNamespace(mandateId, MUNICIPALITY_ID, NAMESPACE);
		verifyNoInteractions(mockMapper);
	}
}
