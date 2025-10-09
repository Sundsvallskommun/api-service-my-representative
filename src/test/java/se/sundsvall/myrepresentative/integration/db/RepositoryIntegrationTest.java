package se.sundsvall.myrepresentative.integration.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static se.sundsvall.myrepresentative.TestObjectFactory.MUNICIPALITY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.NAMESPACE;
import static se.sundsvall.myrepresentative.TestObjectFactory.createMandate;
import static se.sundsvall.myrepresentative.TestObjectFactory.createMandateEntity;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import se.sundsvall.myrepresentative.api.model.SearchMandateParameters;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;

@ExtendWith(MockitoExtension.class)
class RepositoryIntegrationTest {

	@Mock
	private DatabaseMapper mockMapper;

	@Mock
	private MandateRepository mockRepository;

	@InjectMocks
	private RepositoryIntegration repositoryIntegration;

	@AfterEach
	void tearDown() {
		verifyNoMoreInteractions(mockMapper, mockRepository);
	}

	@Test
	void testCreateMandate() {
		final var request = createMandate();
		final var entity = createMandateEntity(false);
		when(mockMapper.toMandateEntity(MUNICIPALITY_ID, NAMESPACE, request)).thenReturn(entity);
		when(mockRepository.save(entity)).thenReturn(entity);

		var mandateEntity = repositoryIntegration.createMandate(MUNICIPALITY_ID, NAMESPACE, request);

		assertThat(mandateEntity).isEqualTo(entity);

		verify(mockMapper).toMandateEntity(MUNICIPALITY_ID, NAMESPACE, request);
		verify(mockRepository).save(entity);
	}

	@Test
	void testGetMandateDetails() {
		final var mandateId = UUID.randomUUID().toString();
		final var entity = createMandateEntity(false);
		when(mockRepository.findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, mandateId)).thenReturn(Optional.of(entity));

		var result = repositoryIntegration.getMandateDetails(MUNICIPALITY_ID, NAMESPACE, mandateId);

		assertThat(result).isPresent().contains(entity);

		verify(mockRepository).findByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, mandateId);
	}

	@Test
	void testDeleteMandate() {
		final var mandateId = UUID.randomUUID().toString();
		var mockEntity = mock(MandateEntity.class);
		when(mockRepository.findActiveByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, mandateId)).thenReturn(Optional.of(mockEntity));

		repositoryIntegration.deleteMandate(MUNICIPALITY_ID, NAMESPACE, mandateId);

		var captor = ArgumentCaptor.forClass(OffsetDateTime.class);
		verify(mockEntity).withDeleted(captor.capture());

		var deletedValue = captor.getValue();

		// Verify that it's an OffsetDateTime (by parsing it) and that it's close to now.
		assertThat(deletedValue).isCloseTo(OffsetDateTime.now(), within(2, ChronoUnit.SECONDS));

		verify(mockRepository).findActiveByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, mandateId);
		verify(mockRepository).save(mockEntity);

		verifyNoMoreInteractions(mockEntity);
		verifyNoInteractions(mockMapper);
	}

	@Test
	void testDeleteMandate_shouldNotDeleteAlreadyDeletedOrMissing() {
		final var mandateId = UUID.randomUUID().toString();
		when(mockRepository.findActiveByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, mandateId)).thenReturn(Optional.empty());

		repositoryIntegration.deleteMandate(MUNICIPALITY_ID, NAMESPACE, mandateId);

		verify(mockRepository).findActiveByMunicipalityIdAndNamespaceAndId(MUNICIPALITY_ID, NAMESPACE, mandateId);
		verifyNoInteractions(mockMapper);
	}

	@Test
	void testSearchMandates() {
		var parameters = new SearchMandateParameters()
			.withGranteePartyId(UUID.randomUUID().toString())
			.withGrantorPartyId(UUID.randomUUID().toString())
			.withSignatoryPartyId(UUID.randomUUID().toString())
			.withPage(1)
			.withLimit(15);

		final var pageable = PageRequest.of(parameters.getPage(), parameters.getLimit());
		final var mandateEntity = createMandateEntity(false);
		final var page = new PageImpl<>(List.of(mandateEntity));

		when(mockRepository.findAllWithParameters(MUNICIPALITY_ID, NAMESPACE, parameters, pageable)).thenReturn(page);

		var pageEntity = repositoryIntegration.searchMandates(MUNICIPALITY_ID, NAMESPACE, parameters);

		assertThat(pageEntity).isEqualTo(page);

		verify(mockRepository).findAllWithParameters(MUNICIPALITY_ID, NAMESPACE, parameters, pageable);
		verifyNoInteractions(mockMapper);
	}
}
