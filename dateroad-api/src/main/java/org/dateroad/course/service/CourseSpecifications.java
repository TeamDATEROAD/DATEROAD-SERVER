package org.dateroad.course.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import org.dateroad.course.dto.request.CourseGetAllReq;
import org.dateroad.date.domain.Course;
import org.dateroad.date.domain.Region;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecifications {
    public static Specification<Course> filterByCriteria(CourseGetAllReq courseGetAllReq) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            addPredicate(predicates, criteriaBuilder, root, "city", courseGetAllReq.city(),
                    (path, value) -> {
                        if (value == Region.SubRegion.SEOUL_ENTIRE || value == Region.SubRegion.GYEONGGI_ENTIRE || value == Region.SubRegion.INCHEON_ENTIRE) {
                            return null;
                        }
                        return criteriaBuilder.equal(path, value);
                    });
            addPredicate(predicates, criteriaBuilder, root, "country", courseGetAllReq.country(),
                    criteriaBuilder::equal);
            addCostPredicate(predicates, criteriaBuilder, root, courseGetAllReq.cost());
            predicates.removeIf(Objects::isNull);
            query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static <T> void addPredicate(List<Predicate> predicates, CriteriaBuilder criteriaBuilder, Root<?> root,
                                         String attributeName, T value,
                                         BiFunction<Path<T>, T, Predicate> predicateFunction) {
        Optional.ofNullable(value)
                .ifPresent(val -> {
                    Predicate predicate = predicateFunction.apply(root.get(attributeName), val);
                    if (predicate != null) {
                        predicates.add(predicate);
                    }
                });
    }

    private static void addCostPredicate(List<Predicate> predicates, CriteriaBuilder criteriaBuilder, Root<?> root,
                                         Integer cost) {
        if (cost != null) {
            switch (cost) {
                case 3:
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("cost"), 30000));
                    break;
                case 5:
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("cost"), 50000));
                    break;
                case 10:
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("cost"), 100000));
                    break;
                case 11:
                    predicates.add(criteriaBuilder.greaterThan(root.get("cost"), 100000));
                    break;
                default:
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("cost"), cost));
                    break;
            }
        }
    }
}

