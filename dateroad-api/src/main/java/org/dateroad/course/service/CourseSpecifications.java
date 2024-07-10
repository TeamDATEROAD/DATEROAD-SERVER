package org.dateroad.course.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import org.dateroad.course.dto.request.CourseGetAllReq;
import org.dateroad.date.domain.Course;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecifications {
    public static Specification<Course> filterByCriteria(CourseGetAllReq courseGetAllReq) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            addPredicate(predicates, criteriaBuilder, root, "city", courseGetAllReq.city(), criteriaBuilder::equal);
            addPredicate(predicates, criteriaBuilder, root, "country", courseGetAllReq.country(), criteriaBuilder::equal);
            addPredicate(predicates, criteriaBuilder, root, "cost", courseGetAllReq.cost(), criteriaBuilder::lessThanOrEqualTo);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static <T> void addPredicate(List<Predicate> predicates, CriteriaBuilder criteriaBuilder, Root<?> root,
                                         String attributeName, T value,
                                         BiFunction<Path<T>, T, Predicate> predicateFunction) {
        Optional.ofNullable(value)
                .ifPresent(val -> predicates.add(
                        predicateFunction.apply(root.get(attributeName), val))
                );
    }
}
