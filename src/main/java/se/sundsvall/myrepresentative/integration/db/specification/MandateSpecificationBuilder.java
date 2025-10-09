package se.sundsvall.myrepresentative.integration.db.specification;

import static java.util.Objects.nonNull;

import org.springframework.data.jpa.domain.Specification;

public class MandateSpecificationBuilder<T> {

	public Specification<T> buildEqualFilter(String attribute, Object value) {
		return (entity, cq, cb) -> nonNull(value) ? cb.equal(entity.get(attribute), value) : cb.and();
	}
}
