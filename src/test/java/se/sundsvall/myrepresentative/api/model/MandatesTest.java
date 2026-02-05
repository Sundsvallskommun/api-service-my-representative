package se.sundsvall.myrepresentative.api.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.sundsvall.dept44.models.api.paging.PagingAndSortingMetaData;

class MandatesTest {

	private static final List<MandateDetails> MANDATE_DETAILS_LIST = List.of(MandateDetailsBuilder.create().build());
	private static final PagingAndSortingMetaData META_DATA = new PagingAndSortingMetaData();

	@Test
	void testConstructor() {
		final var mandates = new Mandates(MANDATE_DETAILS_LIST, META_DATA);
		assertBean(mandates);
	}

	@Test
	void testBuilder() {
		final var mandates = MandatesBuilder.create()
			.withMandateDetailsList(MANDATE_DETAILS_LIST)
			.withMetaData(META_DATA)
			.build();

		assertBean(mandates);
	}

	@Test
	void noDirtOnEmptyBean() {
		assertThat(new Mandates(null, null)).hasAllNullFieldsOrProperties();
		assertThat(MandatesBuilder.create().build()).hasAllNullFieldsOrProperties();
	}

	private void assertBean(Mandates mandates) {
		assertThat(mandates.mandateDetailsList()).isEqualTo(MANDATE_DETAILS_LIST);
		assertThat(mandates.metaData()).isEqualTo(META_DATA);

		assertThat(mandates).hasNoNullFieldsOrProperties();
	}
}
