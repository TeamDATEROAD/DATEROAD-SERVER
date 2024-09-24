package org.dateroad.course.dto.request;

import jakarta.validation.constraints.Min;
import org.dateroad.date.domain.Region;

public record CourseGetAllReq(
        Region.MainRegion country,
        Region.SubRegion city,
        @Min(-1)
        Integer cost
) {
        @Override
        public String toString() {
                return "country=" + country +
                        ", city=" + city +
                        ", cost=" + cost +
                        '}';
        }
}
