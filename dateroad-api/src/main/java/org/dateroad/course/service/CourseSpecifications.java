package org.dateroad.course.service;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.dateroad.course.dto.request.CourseGetAllReq;
import org.dateroad.date.domain.Course;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecifications {
    public static Specification<Course> filterByCriteria(CourseGetAllReq courseGetAllReq) {
        String city = courseGetAllReq.city();
        String country = courseGetAllReq.country();
        Integer cost = courseGetAllReq.cost();
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (city != null && !city.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("city"), city));
            }

            if (country != null && !country.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("country"), country));
            }

            if (cost != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("cost"), cost));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
