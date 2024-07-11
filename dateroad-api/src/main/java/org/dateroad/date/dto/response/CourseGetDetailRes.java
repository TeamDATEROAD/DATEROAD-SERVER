package org.dateroad.date.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.dateroad.date.domain.Course;
import org.dateroad.tag.domain.CourseTag;
import org.dateroad.tag.domain.DateTag;
import org.dateroad.tag.domain.DateTagType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Builder(access = AccessLevel.PRIVATE)
public record CourseGetDetailRes(
        Long courseId,
        List<ImagesList> images,
        int like,
        float totalTime,
        LocalDate date,
        String city,
        String title,
        String description,
        LocalTime startAt,
        List<Places> places,
        int totalCost,
        List<CourseTag> tags,
        boolean isAccess,
        int free,
        int totalPoint
) {

    public static CourseGetDetailRes of(Long courseId,
                                        List<ImagesList> images,
                                        int like,
                                        float totalTime,
                                        LocalDate date,
                                        String city,
                                        String title,
                                        String description,
                                        LocalTime startAt,
                                        List<Places> places,
                                        int totalCost,
                                        List<CourseTag> tags,
                                        boolean isAccess,
                                        int free,
                                        int totalPoint) {
        return CourseGetDetailRes.builder()
                .courseId(courseId)
                .images(images)
                .like(like)
                .totalTime(totalTime)
                .date(date)
                .city(city)
                .title(title)
                .description(description)
                .startAt(startAt)
                .places(places)
                .totalCost(totalCost)
                .tags(tags)
                .isAccess(isAccess)
                .free(free)
                .totalPoint(totalPoint)
                .build();
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record ImagesList(
            String imageUrl,
            int sequence
    ) {
        public static ImagesList of(final String imageUrl, final int sequence) {
            return ImagesList.builder()
                    .imageUrl(imageUrl)
                    .sequence(sequence)
                    .build();
        }
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record Places(
            int sequence,
            String title,
            float duration
    ) {
        public static Places of(final int sequence, final String title, final float duration) {
            return Places.builder()
                    .sequence(sequence)
                    .title(title)
                    .duration(duration)
                    .build();
        }
    }

    @Builder(access = AccessLevel.PRIVATE)
    public record CourseTag(
            DateTagType tag
    ) {
        public static CourseTag of(final DateTagType tag) {
            return CourseTag.builder()
                    .tag(tag)
                    .build();
        }
    }
}
