package se.sundsvall.myrepresentative.integration.db.specification;

import static java.util.Objects.nonNull;

import jakarta.persistence.criteria.Predicate;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class MandateSpecificationBuilder<T> {

	public Specification<T> buildEqualFilter(final String attribute, final Object value) {
		return (entity, cq, cb) -> nonNull(value) ? cb.equal(entity.get(attribute), value) : cb.and();
	}

	public Specification<T> buildEqualAnyFilter(final String attribute, final List<String> values) {
		return (entity, cq, cb) -> {
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
