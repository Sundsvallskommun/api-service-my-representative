package se.sundsvall.myrepresentative.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zalando.problem.ThrowableProblem;
import se.sundsvall.myrepresentative.TestObjectFactory;
import se.sundsvall.myrepresentative.api.model.CreateMandate;
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
		final var createMandate = TestObjectFactory.createMandate();
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

	@Test
	void testDeleteMandate() {
		final var mandateId = UUID.randomUUID().toString();
		var mockEntity = Mockito.mock(MandateEntity.class);
		when(mockMandateRepository.findActiveByIdAndMunicipalityIdAndNamespace(mandateId, MUNICIPALITY_ID, NAMESPACE)).thenReturn(Optional.of(mockEntity));

		representativesService.deleteMandate(MUNICIPALITY_ID, NAMESPACE, mandateId);

		var captor = ArgumentCaptor.forClass(OffsetDateTime.class);
		verify(mockEntity).withDeleted(captor.capture());

		var deletedValue = captor.getValue();

		// Verify that it's an OffsetDateTime (by parsing it) and that it's close to now.
		assertThat(deletedValue).isCloseTo(OffsetDateTime.now(), within(2, ChronoUnit.SECONDS));

		verify(mockMandateRepository).findActiveByIdAndMunicipalityIdAndNamespace(mandateId, MUNICIPALITY_ID, NAMESPACE);
		verify(mockMandateRepository).save(any(MandateEntity.class));

		verifyNoMoreInteractions(mockEntity);
		verifyNoInteractions(mockMapper);
	}

	@Test
	void testDeleteMandate_shouldNotDeleteAlreadyDeleted() {
		final var mandateId = UUID.randomUUID().toString();
		when(mockMandateRepository.findActiveByIdAndMunicipalityIdAndNamespace(mandateId, MUNICIPALITY_ID, NAMESPACE)).thenReturn(Optional.empty());

		representativesService.deleteMandate(MUNICIPALITY_ID, NAMESPACE, mandateId);

		verify(mockMandateRepository).findActiveByIdAndMunicipalityIdAndNamespace(mandateId, MUNICIPALITY_ID, NAMESPACE);
		verifyNoInteractions(mockMapper);
	}

	@Test
	void test() {
		var min = OffsetDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
		System.out.println(min);
	}
}
