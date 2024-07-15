package org.dateroad.course.dto.request;

import org.dateroad.date.domain.Region;

public record CourseGetAllReq(
        Region.MainRegion country,
        Region.SubRegion city,
        Integer cost
) {
}
