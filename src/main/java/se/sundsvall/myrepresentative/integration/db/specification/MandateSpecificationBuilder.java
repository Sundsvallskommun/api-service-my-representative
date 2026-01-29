package se.sundsvall.myrepresentative.integration.db.specification;

import static java.util.Objects.nonNull;

import jakarta.persistence.criteria.Predicate;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class MandateSpecificationBuilder<T> {

	public Specification<T> buildEqualFilter(final String attribute, final Object value) {
		return (entity, _, cb) -> nonNull(value) ? cb.equal(entity.get(attribute), value) : cb.and();
	}

	public Specification<T> buildEqualAnyFilter(final String attribute, final List<String> values) {
		return (entity, _, cb) -> {
			if (values == null || values.isEmpty()) {
				return cb.and();
			}
			final var predicates = values.stream()
				.filter(value1 -> value1 != null && !value1.isBlank())
				.map(value1 -> cb.equal(entity.get(attribute), value1))
				.toArray(Predicate[]::new);
			return cb.or(predicates);
		};
	}
}
