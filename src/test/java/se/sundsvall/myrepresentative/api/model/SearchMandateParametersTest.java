package se.sundsvall.myrepresentative.api.model;

import java.util.List;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.allOf;
import static se.sundsvall.myrepresentative.TestObjectFactory.GRANTEE_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.GRANTOR_PARTY_ID;
import static se.sundsvall.myrepresentative.TestObjectFactory.SIGNATORY_PARTY_ID;

class SearchMandateParametersTest {

	@Test
	void testBean() {
		MatcherAssert.assertThat(SearchMandateParameters.class, allOf(
			hasValidBeanConstructor(),
			hasValidGettersAndSetters(),
			hasValidBeanHashCode(),
			hasValidBeanEquals(),
			hasValidBeanToString()));
	}

	@Test
	void testBuilders() {
		final var parameters = new SearchMandateParameters()
			.withGrantorPartyId(GRANTOR_PARTY_ID)
			.withGranteePartyId(GRANTEE_PARTY_ID)
			.withSignatoryPartyId(SIGNATORY_PARTY_ID)
			.withStatuses(List.of(MandateStatus.EXPIRED.name()))
			.withLimit(100)
			.withPage(1);

		assertThat(parameters.getGrantorPartyId()).isEqualTo(GRANTOR_PARTY_ID);
		assertThat(parameters.getGranteePartyId()).isEqualTo(GRANTEE_PARTY_ID);
		assertThat(parameters.getSignatoryPartyId()).isEqualTo(SIGNATORY_PARTY_ID);
		assertThat(parameters.getStatuses()).containsExactly(MandateStatus.EXPIRED.name());
		assertThat(parameters.getLimit()).isEqualTo(100);
		assertThat(parameters.getPage()).isEqualTo(1);

		assertThat(parameters).hasNoNullFieldsOrProperties();
	}

	@Test
	void testNoDirtOnCreatedBean() {
		assertThat(new SearchMandateParameters()).hasAllNullFieldsOrPropertiesExcept("page", "limit");
	}
}
