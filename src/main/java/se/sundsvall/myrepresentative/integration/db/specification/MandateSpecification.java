package se.sundsvall.myrepresentative.integration.db.specification;

import static java.util.Collections.emptyList;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity;
import se.sundsvall.myrepresentative.integration.db.entity.MandateEntity_;

public class MandateSpecification {

	private static final MandateSpecificationBuilder<MandateEntity> BUILDER = new MandateSpecificationBuilder<>();

	private MandateSpecification() {
		// Prevent instantiation
	}

	public static Specification<MandateEntity> withMunicipalityId(final String value) {
		return BUILDER.buildEqualFilter(MandateEntity_.MUNICIPALITY_ID, value);
	}

	public static Specification<MandateEntity> withNamespace(final String value) {
		return BUILDER.buildEqualFilter(MandateEntity_.NAMESPACE, value);
	}

	public static Specification<MandateEntity> withGrantorPartyId(final String value) {
		return BUILDER.buildEqualFilter(MandateEntity_.GRANTOR_PARTY_ID, value);
	}

	public static Specification<MandateEntity> withSignatoryPartyId(final String value) {
		return BUILDER.buildEqualFilter(MandateEntity_.SIGNATORY_PARTY_ID, value);
	}

	public static Specification<MandateEntity> withGranteePartyId(final String value) {
		return BUILDER.buildEqualFilter(MandateEntity_.GRANTEE_PARTY_ID, value);
	}

	public static Specification<MandateEntity> withStatuses(final List<String> statuses) {
		return BUILDER.buildEqualAnyFilter(MandateEntity_.STATUS, Optional.ofNullable(statuses).orElse(emptyList()).stream().map(Objects::toString).toList());
	}
}
