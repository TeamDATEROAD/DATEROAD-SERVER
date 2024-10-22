package org.dateroad.course.dto;

import java.util.List;
import lombok.Builder;
import org.dateroad.place.domain.CoursePlace;
import org.dateroad.tag.domain.CourseTag;


@Builder
public record CourseWithPlacesAndTagsDto(List<CoursePlace> coursePlaces, List<CourseTag> courseTags) {
    public static CourseWithPlacesAndTagsDto of(List<CoursePlace> coursePlaces, List<CourseTag> courseTags) {
        return CourseWithPlacesAndTagsDto.builder().coursePlaces(coursePlaces).courseTags(courseTags).build();
    }
}
