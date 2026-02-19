package se.sundsvall.myrepresentative.integration.db.specification;

import jakarta.persistence.criteria.Predicate;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

import static java.util.Objects.nonNull;

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
				.filter(value -> value != null && !value.isBlank())
				.map(value -> cb.equal(entity.get(attribute), value))
				.toArray(Predicate[]::new);
			return cb.or(predicates);
		};
	}
}
