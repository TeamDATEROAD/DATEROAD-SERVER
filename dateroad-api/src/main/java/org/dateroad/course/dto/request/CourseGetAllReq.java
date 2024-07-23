package org.dateroad.course.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.dateroad.date.domain.Region;

public record CourseGetAllReq(
        Region.MainRegion country,
        Region.SubRegion city,
        @Min(-1)
        Integer cost
) {
}
